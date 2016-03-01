#!/bin/bash

CLIENT_TOKEN1=c02uZes1eiU:APA91bEUbdjAZ2oq8VM-e6p7GwioQ5l7vonTF3l2bG3x47-pAz59M_rriIOCZTx08M1h0uRvNfbpufxCUpZgZrWrLJXAMbKAEo9eBDiQCbrbvRQupRQ18upXvC0vUf_n_IBfsoDrkKjE
CLIENT_TOKEN2=dAvFk7H1Ae0:APA91bF7Of34lvacLK6YikBKPLJQKuy_3pPMNNp3aFXZObOMTjbPnCnyIPJe-FGS340lryWkSoEEnAowyuct5sEhijCqZvoohBo0t_c9xKvsx7jz6dRSiS8yrptgc4TZZj62QtMy8csX
CLIENT_TOKEN3=dLoA8GMsZcA:APA91bFjhVAXMzkAUScHiRbnxqtQpQVWrdoPTyxMmSK7wih3NQRAj7f2DyqHXl09_BZoU8Am99xss1MFeM_pauAjgRe2ddTLsOHSowXIr3J6wfr-_GKWXF3cppSZCfeRVkFhezgU7oM4

curl -i -H "Content-Type:application/json" -H "Authorization:key=AIzaSyC6w7whFPFYBdmQqJ4Nf8EW8IcFmjSNbfk" -X POST -d "{\"data\":{\"message\":\"$1\", \"time\":\"$(date)\"},\"to\":\"$CLIENT_TOKEN3\"}" https://gcm-http.googleapis.com/gcm/send
