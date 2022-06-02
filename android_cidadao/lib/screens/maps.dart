import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_polyline_points/flutter_polyline_points.dart';
import 'package:maps_flutter/screens/ticket.dart';
import 'package:cloud_functions/cloud_functions.dart';
import 'dart:convert';

class Maps extends StatefulWidget {
  Maps({Key? key}) : super(key: key);

  @override
  _mapsState createState() => _mapsState();
}

class _mapsState extends State<Maps> {
  late GoogleMapController mapController;
  final List<LatLng> geoPoints = [];
  final List<LatLng> polygonCoords = [];
  PolylinePoints polylinePoints = PolylinePoints();
  Set<Polygon> _polygon = Set<Polygon>();

  Future<void> _onMapCreated(GoogleMapController controller) async {
    mapController = controller;
    await getFunction();
    setPolygon(geoPoints);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Stack(
          children:[
            GoogleMap(
              onMapCreated: _onMapCreated,
              initialCameraPosition: const CameraPosition(
                target: LatLng(-22.927395, -47.070410),
                zoom: 15.0,
              ),
              polygons: _polygon,
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: Stack(
                children: [
                  Stack(
                    alignment: Alignment.center,
                    children: [
                      Positioned(
                        child: Container(
                          color: Colors.white,
                          width: MediaQuery.of(context).size.width,
                          height: 90,
                        ),
                      ),
                      Positioned(
                          top: 25,
                          width: 250,
                          child: TextButton(
                            style: TextButton.styleFrom(
                              backgroundColor: Colors.blue,
                              primary: Colors.white,
                              textStyle: const TextStyle(
                                fontSize: 20,
                              ),
                            ),
                            onPressed: () {
                              Navigator.push(
                                  context,
                                  MaterialPageRoute(builder: (context) => Ticket())
                              );
                            },
                            child: Text('Comprar Ticket'),
                          )
                      ),

                    ],
                  ),
                  Positioned(
                    child: Container(
                      color: Colors.blue,
                      width: MediaQuery.of(context).size.width,
                      height: 15,
                    ),
                  ),
                ],
              )
            )
          ],
        ),
      ),
    );
  }

  Future<void> getFunction() async {
    HttpsCallable callable = FirebaseFunctions.instanceFor(region: "southamerica-east1").httpsCallable("getPoints");
    final results = await callable.call(<String, dynamic>{});
    final markers = results.data;

    geoPoints.add(LatLng(markers[0]['marker 1']['_latitude'],markers[0]['marker 1']['_longitude']));
    geoPoints.add(LatLng(markers[0]['marker 2']['_latitude'],markers[0]['marker 2']['_longitude']));
    geoPoints.add(LatLng(markers[0]['marker 3']['_latitude'],markers[0]['marker 3']['_longitude']));
    geoPoints.add(LatLng(markers[0]['marker 4']['_latitude'],markers[0]['marker 4']['_longitude']));

  }


  void setPolygon(List<LatLng> geoPoints) async {
    var i = 0;
    while( i < geoPoints.length - 1) {
      PolylineResult result = await polylinePoints.getRouteBetweenCoordinates(
          "AIzaSyCpGe9tvLJgzYdK4kWevYbG4re9f_zyuik",
          PointLatLng(geoPoints[i].latitude, geoPoints[i].longitude),
          PointLatLng(geoPoints[i+1].latitude, geoPoints[i+1].longitude)
      );

      if (result.status == 'OK') {
        result.points.forEach((PointLatLng point) {
          polygonCoords.add(LatLng(point.latitude, point.longitude));
        });
      }
      i++;
    }
    setState(() {
      /*_polylines.add(
          Polyline(
              width: 5,
              polylineId: PolylineId('Route'),
              color: Colors.blue,
              points: polygonCoords
          )
      );*/
      _polygon.add(Polygon(
        polygonId: PolygonId('Route 1'),
        points: polygonCoords,
        strokeWidth: 4,
        strokeColor: Colors.blue,
        fillColor: Colors.blueAccent.withOpacity(0.5),
        consumeTapEvents: true,
        onTap: (){
          final AlertDialog dialog = AlertDialog(
            title: Text("Informações da área"),
            content: Text("Horário: 8:00 ás 18:00\n\nPreço por hora: R\$1,00"),
          );
          showDialog(context: context, builder: (context) => dialog);
        },
      )
      );
    });
  }
}

