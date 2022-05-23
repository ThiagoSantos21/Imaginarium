import 'package:flutter/material.dart';
import 'package:maps_flutter/screens/maps.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Maps()
    );
  }
}


