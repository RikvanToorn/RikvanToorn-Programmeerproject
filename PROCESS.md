#Process

##week 1

###dag 1
-Ik had al een idee voor een applicatie dus nadat dat wat beter te hebben uitgedacht heb ik het in het proposal document gezet.

###dag 2
-Ik heb het idee beter in woorden duidelijk gemaakt.
-Onderzoek gedaan naar wat er mogelijk is binnen mijn skillset.
-API's gezocht en documentatie snel doorgelezen.

###dag 3
-Design van mijn proposal document verbeterd, uitgebreid en netter ontworpen.
-Laatste keer idee lichtelijk aangepast.
-MVP vastgesteld.

###dag 4
-Begonnen met alle schermen te ontwerpen en de teksten te bedenken.
-Navigatie volledig werkend gemaakt.
-Verbonden met Google Maps API.

###dag 5
-Presentatie gegeven over prototype
-Schermen afgemaakt.

##week 2

###dag 6
-Google Places API werkend gekregen.
-Schermen aangepast en gefinetuned.
-Inloggen en registreren met firebase geimplementeerd.

###dag 7
-Besloten 'urgency' voorlopig nog weg te laten omdat het weinig toegevoegde waarde heeft.
-Formulier op de AddReminderActivity werkend gekregen en het succesvol opgeslagen in firedatabase

###dag 8 (jarig/werk - bijna niks gedaan)
-Begin gemaakt aan het ophalen van de gegevens om het in een listview te zetten.

###dag 9
-Hele dag bezig geweest met juist ophalen van de gegevens uit de firedatabase
-Omdat het verschillende soorten variabelen zijn en zelfs een LatLng (longtitude / latitude) object had ik hier erg veel moeite mee. Uiteindelijk is het me gelukt maar wel met een omweg (denk ik).
assistentie kwam er ook niet uit.

###dag 10
-Presentatie gegeven.
-Onderzocht hoe ik google maps API ga gebruiken om op het juiste moment de meldingen te versturen.

##week 3
###dag 11
-Bezig geweest met het ophalen van de GPS locatie van de telefoon. veel problemen mee gehad maar uiteindelijk met de hulp van youtube videos een recent goed voorbeeld gevonden.
-Gelijk toegevoegd dat de gebruiker om toestemming word gevraagd voor de GPS locatie en dat een instellingen scherm automatisch in beeld komt als 'Locatie' uistaat.
-Overwegen om toch niet de optie toe te voegen dat gebruikers een melding krijgen wanneer ze een locatie verlaten.

###dag 12
-volgende stap is opgehaalde locatie vergelijken met locatie uit de database. Lang mee bezig geweest. kreeg het niet voor elkaar een goede afstand in meters te verkrijgen.
-Uiteindelijk bij een goede oplossing gekomen die de afstand in meters tussen de huidige locaties en alle locaties van reminders uit de database verkrijgt.
-Morgen dit vergelijken met de 'afstand tot locatie' van dezelfde reminders uit de database.

##dag 13
- getest met Toasts om te kijken of ik op de goede manier de afstand vergelijk. Dit lijkt te kloppen als ik de de afstand op google maps bekijk.
- volgende stap is dit op de achtergrond te laten draaien op alle activiteiten. dit gaat helaas niet zomaar. Ik kan hem wel bij elke activiteit opnieuw starten. ik besluit dit te doen en daarom dus notificatie bij gebied verlaten helemaal weg te laten.
- gezocht naar een manier om dit ook te laten draaien wanneer de app is afgesloten. ik kom hierbij op het concept van een 'service' dit blijkt ook gewoon te werken wanneer de app is afgesloten volgens de documentatie. hier ga ik morgen direct aan beginnen.

##dag 14
- Hele dag bezig geweest met het werkend krijgen van de service en alle processen die hier op moeten draaien. Hoewel ik hier de hele dag mee bezig ben geweest gaat dit verbazend makkelijk af. ik overweeg nu weer op de melding bij de locatie verlaten toch weer toe te voegen.
- Ik krijg nu wel constant notifcaties wanneer ik binnen het gebied rondloopt. het goede nieuws is wel dat dit ook werkt wanneer ik de applicatie volledig afsluit. ik besluit dit later op te lossen.
- Ook vandaag nog voor elkaar gekregen dat wanneer je op de notificatie klikt je naar de juiste activity gaat. Nu moet hierin nog alle juiste informatie uit de database komen te staan.
 
###dag 15
- presentatie geven met werkende notificaties op het juiste moment.
- Vast gekeken naar het voorkomen m=van meerdere notificaties . dit is net als voorhee erg vervelend testen omdat ik genoodzaakt rond te lopen met mijn mobiele telefoon zonder dat ik de foutmeldingen kan bekijken in android studio.

##week 4
###dag 16
- Probleem opgelost van de meerdere notificaties door een simpele toevoeging.
- Toch besloten notificaties wanneer je het gebief verlaat toe te voegen omdat me iets te binnen schoot wat wel zou kunnen werken. dit werkte gelukkig ook.
- applicatie is zo goed als af. begonnen met het refacteren van de code wat ook nodig is om laatste kleine dingen werkend te kirjgen zoals de informatie van 1 reminder ophalen uit de database inplaats van allemaal.  
Ik loop tegen het feit aan dat firebase asynchoniseus werkt en daardoor pas na mijn return statement klaar is. dit geeft een leeg reminder object terug waardoor ik er niks mee kan. na een lange tijd nog niet opgelost dus besloten dit de volgende dag aan de assistensten op school te vragen.

###dag 17
- Op school begonnen met mijn eindverslag en het toevoegen/aanvullen van comments in mijn code. wachten op assistenten voor (hopelijk) mijn laatste probleem.

###dag 18

###dag 19

###dag 20
