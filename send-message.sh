#!/bin/bash

curl -i -H "Content-Type:application/json" -H "Authorization:key=AIzaSyC6w7whFPFYBdmQqJ4Nf8EW8IcFmjSNbfk" -X POST -d "{\"data\":{\"message\":\"$1\", \"time\":\"$(date)\"},\"to\":\"cuXmUuY2j9g:APA91bGb4EYu3DVjS-HjEIXfjz_eQnJLB2jSjzgiv19GP6reyGi9pMOFYkLTbiDYfoqr1C253UgaSK2KXTPSS__6HnGBa1wP25Hi27W59CId-AhA4WSAmgrOQDyrKiqb3ZnRpmYzS_nD\"}" https://gcm-http.googleapis.com/gcm/send