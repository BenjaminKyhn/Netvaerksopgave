# Netvaerksopgave

## Implementeringsstrategi
Vi vil skrive en socket server i python, som skal køre på en Raspberry Pi. Serveren indsamler informationer om temperatur og luftfugtighed fra Pi'en vha. en DHT22 sensor, som skal
forbindes fysisk til Pi'en. Vi laver en klient i Java, som skaber forbindelse til serveren og modtager data'ene om temperatur og luftfugtighed.
Vi vil lave et objekt, som indeholder alle data'ene om temperatur, luftfugtighed og tidspunkt, sådan så klienten kan modtage alle de nødvendige data samlet. Når data'ene er
modtaget på klientsiden, skal de fremstilles med en kurve. Vi tænker at tegne den i JavaFX. Dette kan gøres med en polyline med punkter, som skabes dynamisk ud fra den data,
som modtages periodisk fra Pi'en.

## Protokoller
Data bliver sendt med TCP-protokollen, fordi det er en meget lille mængde data, som sendes i intervaller. Dvs. den samlede strøm af data bliver så lille, at der ikke er nogle
ulemper ved at bruge TCP, selvom den er langsommere end UDP. Til gengæld sikrer vi os, at data'ene er korrekte, da TCP gør brug af three-way-handshake.
