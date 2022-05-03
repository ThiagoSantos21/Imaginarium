import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const ticket = db.collection("ticket");
const itinerary = db.collection("itinerary");

interface CallableResponse {
  status: string,
  message: string,
  payload: JSON,
}

interface Ticket {
  placa: string,
  horaEntrada: FirebaseFirestore.Timestamp,
  horaSaida: FirebaseFirestore.Timestamp,
}

/**
*Função que faz a validação do veículo
*@param {Ticket} t
*dados do veículo no firestore database
*@return {number}
*Retorna 0 caso não tenha o veículo
*1 caso o veículo esteja inválido
*2 caso o veículo esteja válido
*/
function errorCode(t: Ticket): number {
  if (t.placa == null) {
    return 3;
  }
  return 5;
}

/**
 * Função que determina a mensagem a ser enviada
 * @param {number} valid
 * Resultado da função isValid
 * @return {string}
 * Retorna a mensagem de acordo com o resultado da função isValid
*/
function errorMessage(valid: number): string {
  let message = "";

  switch (valid) {
    case 0: {
      message = "Veículo não encontrado";
      break;
    }
    case 1: {
      message = "Veículo inválido";
      break;
    }
    case 2: {
      message = "Veículo encontrado";
      break;
    }
    case 3: {
      message = "Placa inválida";
      break;
    }
  }
  return message;
}

/**
 * Função que verifica a hora de saida do ticket
 * @param {Ticket} p
 * Ticket que será verificado
 * @return {CallableResponse}
 * Retorna o resultado da validação
*/
function validateTime(p: Ticket): CallableResponse {
  let result: CallableResponse;

  const now: FirebaseFirestore.Timestamp =
  admin.firestore.Timestamp.now();

  if (p.horaSaida.seconds < now.seconds) {
    result = {
      status: "ERROR",
      message: "Tempo expirado",
      payload: JSON.parse(JSON.stringify({
        placa: p.placa,
        horaEntrada: p.horaEntrada,
        horaSaida: p.horaSaida,
      })),
    };
  } else {
    result = {
      status: "SUCCESS",
      message: "Placa válida",
      payload: JSON.parse(JSON.stringify({
        placa: p.placa,
        horaEntrada: p.horaEntrada,
        horaSaida: p.horaSaida,
      })),
    };
  }
  return result;
}

export const addTicket = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      let result: CallableResponse;
      const entrada: FirebaseFirestore.Timestamp =
      admin.firestore.Timestamp.now();

      const saida =
        new admin.firestore
            .Timestamp(entrada.seconds + (data.estadia * 3600)
                , entrada.nanoseconds);

      const t: Ticket = {
        placa: data.placa,
        horaEntrada: entrada,
        horaSaida: saida,
      };
      const errorcode = errorCode(t);
      const errormessage = errorMessage(errorcode);

      if (errorcode == 3 || errorcode == 4) {
        result = {
          status: "ERROR",
          message: errormessage,
          payload: JSON.parse(JSON.stringify({placa: null})),
        };
      } else {
        await ticket.add(t);
        result = {
          status: "SUCCESS",
          message: "Veículo inserido com sucesso",
          payload: JSON.parse(JSON.stringify({placa: t.placa})),
        };
      }

      return result;
    });

/* export const addMarker = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      const m: Marker = {
        Name: data.Name,
        LatLng: data.LatLng,
        Address: data.Address,
      };
      await marker.add(m);

      return m;
    }); */

export const searchTicket = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      const snapshot = await ticket.get();
      let result: CallableResponse;

      let p: Ticket;

      result = {
        status: "NOTFOUND",
        message: "Veículo não registrado",
        payload: JSON.parse(JSON.stringify({placa: null})),
      };
      snapshot.forEach((doc) => {
        const d = doc.data();
        const plateTicket: Ticket = {
          placa: d.placa,
          horaEntrada: d.horaEntrada,
          horaSaida: d.horaSaida,
        };

        if (data.placa === plateTicket.placa) {
          p = {
            placa: plateTicket.placa,
            horaEntrada: plateTicket.horaEntrada,
            horaSaida: plateTicket.horaSaida,
          };
          result = validateTime(p);
        }
      });
      functions.logger.info(result.status.toString());
      return result;
    });

/* export const getZonaAzul = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      functions.logger.info("getZonaAzul - Iniciada.");
      const itinerario:Array<ZonaAzul> = []
      const snapshot = await colZonaAzul.get();

      let tempMarker: ZonaAzul;
      snapshot.forEach((doc) => {
        const d = doc.data();
        tempMarker = {
          LatLng: d.LatLng,
          title: d.title,
          snippet: d.snippet
        };
        itinerario.push(tempMarker);
      });
      return itinerario;
    });*/
