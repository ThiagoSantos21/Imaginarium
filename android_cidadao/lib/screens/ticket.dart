import 'dart:ffi';
import 'dart:core';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:maps_flutter/screens/creditcard.dart';
import 'package:maps_flutter/screens/pix.dart';
import 'package:string_validator/string_validator.dart';


class Ticket extends StatefulWidget{
  Ticket({Key? key}) : super(key: key);

  @override
  _ticketState createState() => _ticketState();
}

class _ticketState extends State<Ticket>{
  int selectedValue = 0;
  int selectedPayment = 0;
  String placa = '';
  String cpf = '';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
          body: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            mainAxisSize: MainAxisSize.max,
            children: [
              Padding(
                  padding: EdgeInsets.symmetric(horizontal: 80),
                  child:
                  TextField(
                    onChanged: (text) {
                      placa = text;
                    },
                    decoration: const InputDecoration(
                        hintText: 'Digite a placa'
                    ),
                  )
              ),
              Padding(
                  padding: EdgeInsets.symmetric(horizontal: 80),
                  child:
                  TextField(
                    onChanged: (text) {
                      cpf = text;
                    },
                    decoration: const InputDecoration(
                        hintText: 'Digite o seu CPF'
                    ),
                  )
              ),
              const Text(
                  'Selecione a quantidade de horas',
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  Column(
                    children: [
                      Radio(
                          value: 1,
                          groupValue: selectedValue,
                          onChanged: (value){
                            setState((){
                              selectedValue = value.hashCode.toInt();
                            });
                          }),
                      Text('1 hora')
                    ],
                  ),
                  Column(
                    children: [
                      Radio(
                          value: 2,
                          groupValue: selectedValue,
                          onChanged: (value){
                            setState((){
                              selectedValue = value.hashCode.toInt();
                            });
                          }),
                      Text('2 horas')
                    ],
                  ),
                  Column(
                    children: [
                      Radio(
                          value: 3,
                          groupValue: selectedValue,
                          onChanged: (value){
                            setState((){
                              selectedValue = value.hashCode.toInt();
                            });
                          }),
                      Text('3 horas')
                    ],
                  ),
                  Column(
                    children: [
                      Radio(
                          value: 4,
                          groupValue: selectedValue,
                          onChanged: (value){
                            setState((){
                              selectedValue = value.hashCode.toInt();
                            });
                          }),
                      Text('4 horas')
                    ],
                  ),
                ],
              ),
              Text(
                'Selecione a forma de pagamento',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  Column(
                    children: [
                      Radio(
                          value: 1,
                          groupValue: selectedPayment,
                          onChanged: (value){
                            setState((){
                              selectedPayment = value.hashCode.toInt();
                            });
                          }),
                      Text('PIX')
                    ],
                  ),
                  Column(
                    children: [
                      Radio(
                          value: 2,
                          groupValue: selectedPayment,
                          onChanged: (value){
                            setState((){
                              selectedPayment = value.hashCode.toInt();
                            });
                          }),
                      Text('Cartão de crédito')
                    ],
                  ),

                ],
              ),
              ElevatedButton(
                  onPressed: (){
                    if(selectedValue == 0 || selectedPayment == 0 || placa == '' || cpf.length == '' ){
                      const AlertDialog dialog = AlertDialog(
                        title: Text("Erro", style: TextStyle(color: Colors.blue),),
                        content: Text("Preencha todos os campos"),
                      );
                      showDialog(context: context, builder: (context) => dialog);
                    } else if ( isNumeric(cpf) == false || isAlphanumeric(placa) == false) {
                      const AlertDialog dialog = AlertDialog(
                        title: Text("Erro", style: TextStyle(color: Colors.blue),),
                        content: Text("Campo(s) inválido(s)"),
                      );
                      showDialog(context: context, builder: (context) => dialog);
                    } else {
                      if(selectedPayment == 2 ) {
                        Navigator.push(
                            context,
                            MaterialPageRoute(builder: (context) => creditCard(selectedValue,placa))
                        );
                      }
                      if(selectedPayment == 1){
                        Navigator.push(
                            context,
                            MaterialPageRoute(builder: (context) => pix(selectedValue,placa))
                        );
                      }
                    }
                  },
                  child: Text('Continuar')
              )

            ],
          ),
        )
    );
  }
}
/*class hourButton extends StatelessWidget {
  const hourButton({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return //Button
  }
}*/