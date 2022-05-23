import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Ticket extends StatefulWidget{
  Ticket({Key? key}) : super(key: key);

  @override
  _ticketState createState() => _ticketState();
}

class _ticketState extends State<Ticket>{
  int selectedRadio = 0;
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
          body: Stack(
            children: [
              const Padding(
                  padding: EdgeInsets.symmetric(horizontal: 80, vertical: 50),
                  child:
                  TextField(
                    decoration: InputDecoration(
                        hintText: 'Digite a placa do veículo'
                    ),
                  )
              ),
              Positioned(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      RadioListTile(
                          title: const Text('1 hora'),
                          value: 1,
                          groupValue: selectedRadio,
                          onChanged: (value) {
                            setState((){
                              selectedRadio = value.hashCode.toInt();
                            });
                          }
                      ),
                      RadioListTile(
                          title: const Text('2 horas'),
                          value: 2,
                          groupValue: selectedRadio,
                          onChanged: (value) {
                            setState((){
                              selectedRadio = value.hashCode.toInt();
                            });
                          }
                      ),
                      RadioListTile(
                          title: const Text('3 horas'),
                          value: 3,
                          groupValue: selectedRadio,
                          onChanged: (value) {
                            setState((){
                              selectedRadio = value.hashCode.toInt();
                            });
                          }
                      ),
                      RadioListTile(
                          title: const Text('4 horas'),
                          value: 4,
                          groupValue: selectedRadio,
                          onChanged: (value) {
                            setState((){
                              selectedRadio = value.hashCode.toInt();
                            });
                          }
                      ),
                    ],
                  )
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