# musicmanager
m295 - 
Lara Corrodi \
Music Api - Manager

# Validierungsregeln in der Playlist-Entität

In der Playlist-Entität sind folgende Validierungsregeln definiert:

1. **Playlistname** (String):  
   Maximal 50 Zeichen lang.

2. **Songname** (String):  
   Darf nur aus Buchstaben (A-Z, a-z) und Leerzeichen bestehen, keine Zahlen oder Sonderzeichen.

3. **Erstelldatum** (LocalDate):  
   Das Erstelldatum darf nicht in der Zukunft liegen, es muss in der Vergangenheit oder heute sein.

