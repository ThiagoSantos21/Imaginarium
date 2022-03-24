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
        placa: "ABD1234", // String,
        horaEntrada: "11:30:00", // Date,
        horaSaida: "13:30:00", // Date
        tipoVeiculo: "Carro", // String,
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
      const placa = "ABC1234";
      const snapshot = await ticket.where("placa", "==", placa).get();
      const search : FirebaseFirestore.DocumentData = [];
      snapshot.forEach((doc) => {
        search.push(doc.data());
      });
      response.status(200).json(search);
    });
