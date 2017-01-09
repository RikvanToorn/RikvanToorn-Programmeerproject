# RikvanToorn-Programmeerproject
Applicatie voor het Programmeerproject van Rik van Toorn, 11279184


## Het probleem
Iedereen heeft wel bepaalde dingen die hij of zij moet onthouden. Dit kunnen spullen zijn die men nodig heeft of zaken die nog geregeld moeten worden. Dit kunnen dingen zijn zoals de verwarming uit zetten wanneer je je woning verlaat of je oplader weer meenemen die je de vorige keer bij een vriend hebt laten liggen. Het belangrijkste bij dit soort dingen is dat je er aan denkt op het goede moment of beter gezegd op de goede plek.

Voor dit probleem wil ik dus een applicatie ontwikkelen die gebruikers aan bepaalde zaken herinnerd wanneer deze op een bepaalde plek zijn Op basis van de GPS locatie van de mobiele telefoon kan de applicatie zien waar de gebruiker zich bevind en op basis hiervan de gebruiker een melding sturen die deze er aan herinnerd iets niet te vergeten. Voor dat de applicatie weet wanneer een melding te versturen moet de gebruiker eerst een herinnering toevoegen aan een lijst met herrenneringen. In deze herinnering kan de gebruiker minimaal een titel, beschrijving en locatie invoeren. Ook moet de gebruiker instellen in welke radius van de locatie de melding word weergegeven (minimale afstand tot de locatie om een melding te triggeren). Verder kan de gebruiker aangeven hoe belangrijk het is, Tot welke datum en tijd de herinnering moet blijven staan en of de melding pas moet verschijnen wanneer de gebruiker een bepaalde locatie verlaat om zo niet alsnog iets te vergeten als de gebruiker lang op een bepaalde locatie verblijft en daarna weer vertrekt.

## Applicatie onderdelen
De applicatie zal vooral bestaan uit 4 verschillende schermen. Een hoofdactiviteit waar de gebruiker zijn lijst met herinerringen kan zien. Een activiteit met een formulier waar de gebruiker een nieuwe herinnering kan toevoegen of een bestaande kan aanpassen. En een activiteit waar een kaart op word weergegeven zodat een gebruiker daar ook een herinnering op kan zien door middel van een landmark mits dichtbij genoeg om deze te kunnen zien staan. Als laatste is er een scherm waar de herinerring word weergegeven. De gebruiker komt vanzelf op dit scherm wanneer de app is geopend en op de locatie van een herinnering komt of wanneer de gebruiker op een melding klikt als deze binnenkomt wanneer de applicatie op de achtergrond draait.

## Benodigde API's en tools
Ik zal hoogstwaarschijnlijk gebruik maken van de firebase tool om zo gebruikers de mogelijkheid te geven zich te registreren en in te loggen in de applicatie. Ook zal er dan gebruik worden gemaakt van de database feature om per gebruiker de herinneringen op te slaan. Daarnaast zal ik gebruik maken van een map API en dan waarschijnlijk die van google gezien deze het meest word gebruikt en daarom ook een uitgebreide documentatie beschikbaar heeft. 

Het grootste probleem waar ik verwacht tegen aan te lopen is het draaien van de applicatie op de achtergrond. Hier heb ik nog nooit mee gewerkt en weet ook niet hoe ik dit moet implementeren. Op internet staan gelukkig genoeg tutorials en documentatie om dit probleem op te lossen en succesvol uit te voeren. 

