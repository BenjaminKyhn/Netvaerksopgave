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

## Konklusion
Vi løb ind i nogle problemer under projektet. Da vi ikke har arbejdet med Python før, var der mange udfordringer ift. Pythonsyntaks, og vi kunne ikke rigtig finde nogen andre måder at sende data'ene, end som en String. Det betyder, at som det er lavet lige nu, så bliver alle data'ene gemt i en String, som sendes til klienten. Vi forsøgte at sende
data'ene over flere gange, men det skabte problemer. Derfor skal data'ene formatteres på klientsiden, når de modtages, sådan så vi får temperatur, luftfugtighed og tidspunkt i 3
separate variable. Disse variable bruger vi så til at tegne vores graf.
Vi endte med ikke at bruge JavaFX, da vi fandt et library kaldet JFree Chart, der havde alle de ønskede features og var forholdsvis nemt at gå til. JFree Chart bruger Java Swing
fremfor JavaFX, så derfor endte løsningen med at bruge dette.

![Graf til demo](https://i.imgur.com/2LaVTDz.jpg)
