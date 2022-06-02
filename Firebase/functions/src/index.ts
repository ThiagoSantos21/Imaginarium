import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const ticket = db.collection("ticket");
const itinerary = db.collection("itinerary");
const payment = db.collection("payment");

interface CallableResponse {
  status: string,
  message: string,
  payload: string,
}

interface Ticket {
  placa: string,
  horaEntrada: FirebaseFirestore.Timestamp,
  horaSaida: FirebaseFirestore.Timestamp,
  transactionId: string,
}

interface paymentResponse {
  value: number,
  transactionId: string,
  result: string,
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
        transactionId: p.transactionId,
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
        transactionId: p.transactionId,
      })),
    };
  }
  return result;
}

/**
 * Sorteia um numero inteiro entre o minimo e maximo
 * @param {number} min - numero minimo
 * @param {number} max - numero maximo
 * @return {number} numero sorteado
 */
function getRandomInt(min: number, max: number) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

/**
 * Gera uma string aleatória
 * @param {number} size - Tamanho da string, mínimo é 8 caracteres.
 * @return {string} token ou string vazia (caso de erro)
 */
function generateRandomString(size: number): string {
  let token = "";
  const alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "abcdefghijklmnopqrstuvwxyz0123456789";
  // obtendo um numero (n) inteiro arredondado sempre para baixo.
  const n = Math.floor(size);
  if (n >= 8) {
    let count = 0;
    while (count < n) {
      token += alfabeto.charAt(getRandomInt(0, (alfabeto.length -1)));
      count++;
    }
  }
  return token;
}

/**
 * Função que verifica o pagamento do cartão
 * @param {number} value
 * Valor do pagamento
 * @return {paymentResponse}
 * Retorna o resultado da validação
*/
function paymentSimulator(value: number) {
  let result: paymentResponse;

  if (getRandomInt(0, 1)) {
    result = {
      value: 3*value,
      transactionId: generateRandomString(64),
      result: "TRANSACAO_EFETUADA",
    };
  } else {
    result = {
      value: 3*value,
      transactionId: generateRandomString(64),
      result: "TRANSACAO_NAO_EFETUADA",
    };
  }
  return result;
}

export const addTicket = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      let result: CallableResponse;

      const paymentResult = paymentSimulator(data.estadia);

      payment.add(paymentResult);

      if (data.payment === 1 ||
        paymentResult.result === "TRANSACAO_EFETUADA") {
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
          transactionId: paymentResult.transactionId,
        };

        await ticket.add(t);
        result = {
          status: "SUCCESS",
          message: "Veículo inserido com sucesso",
          payload: paymentResult.transactionId,
        };
      } else {
        result = {
          status: "ERROR",
          message: "Transação não efetuada - Tente outro cartão",
          payload: paymentResult.transactionId,
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
          transactionId: d.transactionId,
        };

        if (data.placa === plateTicket.placa) {
          p = {
            placa: plateTicket.placa,
            horaEntrada: plateTicket.horaEntrada,
            horaSaida: plateTicket.horaSaida,
            transactionId: plateTicket.transactionId,
          };
          result = validateTime(p);
        }
      });

      return result;
    });

export const getPoints = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      const snapshot = await itinerary.get();
      const points:FirebaseFirestore.DocumentData = [];

      snapshot.forEach((doc) => {
        points.push(doc.data());
      });
      return points;
    });

export const pixKey = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      const pix = generateRandomString(32);

      return pix;
    });
