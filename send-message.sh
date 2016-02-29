#!/bin/bash

CLIENT_TOKEN=c02uZes1eiU:APA91bEUbdjAZ2oq8VM-e6p7GwioQ5l7vonTF3l2bG3x47-pAz59M_rriIOCZTx08M1h0uRvNfbpufxCUpZgZrWrLJXAMbKAEo9eBDiQCbrbvRQupRQ18upXvC0vUf_n_IBfsoDrkKjE

curl -i -H "Content-Type:application/json" -H "Authorization:key=AIzaSyC6w7whFPFYBdmQqJ4Nf8EW8IcFmjSNbfk" -X POST -d "{\"data\":{\"message\":\"$1\", \"time\":\"$(date)\"},\"to\":\"$CLIENT_TOKEN\"}" https://gcm-http.googleapis.com/gcm/send