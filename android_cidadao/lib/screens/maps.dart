import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_polyline_points/flutter_polyline_points.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:maps_flutter/screens/ticket.dart';
import 'firebase_options.dart';

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

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
    setGeoPoints();
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

  void setGeoPoints() {
    //Fazer a integração com o Firebase
    geoPoints.add(LatLng(-22.927395, -47.070410));
    geoPoints.add(LatLng(-22.9266411,-47.074078));
    geoPoints.add(LatLng(-22.9301586,-47.0713499));
    geoPoints.add(LatLng(-22.927395, -47.070410));
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
      )
      );
    });
  }
}

