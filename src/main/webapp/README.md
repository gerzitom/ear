# Task manager

Tato aplikace slouží pro firmy, jako správa úkolů pro projekty, které dělají. Můžou zde přidávat projekty, k projektům přidávat úkoly a vytvářet sprinty, ten může vždy existovat jen jeden.

K úkolům si každý uživatel může trackovat čas a komentovat ho. U každého úkolu můžou být i podúkoly.

V aplikaci jsou 2 možné role pro uživatele, jedna je klasický user, který vidí jen projekty a úkoly, do kterých je přidaný a task manager vidí vše.

Jako dokumentace REST API rozhraní jsme využili knihovnu SWAGGER, její iplementace je [zde](http://localhost:8080/swagger-ui.html)

Aplikace je rozdělená na 2 části: frontend a backend.
Na backendu běží Spring a Hibernate
Frontend běží na Frameworku Nuxt.js, který je takovou nadstavbou Vue.js. Projekt používá UI knihovnu Vuetify.

Zabezpečení backendu funguje přes JWT token. Uživatel se jednou přihlásí a tím se mu vygeneruje token, který potom přidává ke všem requestům jako ověření. Podle JWT se také řeší, jaké má uživatel práva a podle toho se mu servíruje obsah.

## Spuštění

### Backend
Pro spuštění backendu je potřeba mít postresql databázi se jménem tm. Přihlašovací údaje jsou defaultní: postgres, postgres.
Potom pouze spustit spring aplikaci, základní data se importují se spuštěním aplikace.

### Frontend
Pro spuštění frontendu je potřeba si nainstalovat Node.js, potom:

```bash
# instalace potřebných dependencí
$ npm install

# Spuštění frameworku Nuxt.js
$ nuxt

```

Aplikace běží na adrese [http://localhost:3000](http://localhost:3000/)


## Zhodnocení projektu
Tento projekt jsme se dost užili. Dali jsme si za cíl vytvořit i jednoduché UI pro aplikaci, které dalo, i tak jednoduché dost zabrat, ale přišlo nám, že takto jsme si vyzkoušeli líp, jak se s REST API pracuje při implementaci.
Hned na začátku jsme měli problém pochopit Asociace mezi entitami, ale nakonec jsme to dostatečné pochopili (Po hovoru se cvičícím). 

Dále potom byl problém s použitím DTO objektů, které se, podle našeho názoru, moc neprobírají na cvičeních a proto jsme si museli hodně informací dohledávat sami.

Dále byl problém s implementací JWT autorizace, ale nakonec se vše podařilo.

Taky nám dalo dost práce vytvořit UI pro aplikaci, aby to alespoň trochu fungovalo, ale hodnotíme to jako hodně dobrou zkušennost. Možná je trošku škoda, že v tomto semestru není předmět, ve kterém bychom mohli tyto zkušennosti uplatnit.

### Tomáš Geržičák
Práce na tomto projektu mě bavila asi nejvíce v tomto semestru a rád bych se o psaní aplikací ve Springu zajímal více. Taky celý nápad s UI pro aplikaci byl můj nápad. No myslím, že jsme si vzali velké sousto, ale z výsledku jsem spokojen, jen škoda, že jsme neměli více času.

