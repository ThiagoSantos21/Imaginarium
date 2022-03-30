import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const ticket = db.collection("ticket");


// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript

export const addTicket = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const t = {
        placa: String,
        horaEntrada: String,
        horaSaida: String,
        tipoVeiculo: String,
      };
      try {
        const docRef = await ticket.add(t);
        response.send("veiculo inserido com sucesso.  " + docRef.id);
      } catch (e) {
        response.send("erro ao inserir veiculo");
      }
    });

export const searchTicket = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const placa = request.query.placa;
      const snapshot = await ticket.where("placa", "==", placa).get();
      const search : FirebaseFirestore.DocumentData = [];
      snapshot.forEach((doc) => {
        search.push(doc.data());
      });
      response.json(search);
    });
