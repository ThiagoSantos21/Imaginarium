import 'package:cloud_functions/cloud_functions.dart';
import 'package:clipboard/clipboard.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'maps.dart';

class pix extends StatefulWidget {
  pix(this.value,this.placa,{Key? key}) : super(key: key);
  final String placa;
  final int value;
  @override
  _pixState createState() => _pixState();
}
String pixKey = '';

class _pixState extends State<pix>{

  void getPixKey() async {
    HttpsCallable callable = FirebaseFunctions.instanceFor(region: "southamerica-east1").httpsCallable("pixKey");
    final results = await callable.call(<String, dynamic>{});
    setState((){
      pixKey = results.data.toString();
    });
    print(widget.placa);
  }

  void addTicket() async {
    HttpsCallable callable = FirebaseFunctions.instanceFor(region: "southamerica-east1").httpsCallable("addTicket");
    final results = await callable.call(<String, dynamic>{
      "placa" : widget.placa,
      "estadia" : widget.value,
      "payment" : 1,
    });
    final AlertDialog dialog = AlertDialog(
      title: Text("Código Copiado!"),
    );
    showDialog(context: context, builder: (context) => dialog);
  }

  @override
  void initState() {
    super.initState();
    getPixKey();
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          body: Align(
            alignment: Alignment.center,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text('Valor: R\$ ${widget.value*3},00\n',style: TextStyle(fontWeight: FontWeight.bold,fontSize: 30),),
                Text('                         Chave PIX\n\n$pixKey\n\n\n',),
                ElevatedButton(
                  onPressed: (){
                    FlutterClipboard.copy(pixKey);
                    addTicket();
                    Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => Maps())
                    );
                  },
                  child: Text('Copiar Código'),
                )
              ],
            )
          )
      )
    );
  }
}




