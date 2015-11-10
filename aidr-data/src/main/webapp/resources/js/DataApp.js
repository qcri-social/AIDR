var app = angular.module('DataApp', ['ngRoute', 'ngResource','ui.bootstrap','ngAnimate']).run(function($rootScope) {
  $rootScope.authenticated = false;
  $rootScope.current_user = '';

  
  
});




app.controller('appCtrl',function($scope, $uibModal, $log ,$filter,$http){
  // $http.get('arc.json')
  //      .then(function(result){
  //         $scope.alphabet = result.data;                
  //       });
  
  $scope.alphabet = 

[

   {

      "code": "aus_oct13_kw",

      "name": "Australia 2013 fire kw",

      "totalCount": 471435,

      "language": "en,en,en,en,en,en,en,en,en,en,en,en,en,",

      "curator": "o_saja",

      "collectionCreationDate": 1382528892000,

      "endDate": 1383152267000,

      "startDate": 1382537395000,

      "keywords": "#australia, #Fires, #nswfires, #NSW, Springwood, wildfire, wildfires, #nswrfs, #gateshead,  #Faulconbridge, #faulconbridge, #bushfire, Linksview Road Fire, NSW Fire, Linksview Rd, Springwood, australia wildfires, Australian wildfire, Australia Destroy, new south wales, evacute,  devastation, stay safe, rescuer, firefighters, firefighter,  warnings, Blue Mountains, assess damage, feared, evacution, bushfires,\nsoutheastern Australia, orange haze, port stephens, Sydney suburbs, #smokedamage, Aussie state, #bushfire, #wildfire, #bushfires,  west of Sydney, force evacuations, outskirts of Sydney, australia",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "stjude_oct-13",

      "name": "StJude",

      "totalCount": 75,

      "language": "en",

      "curator": "GlobalFloodNews",

      "collectionCreationDate": 1383120813000,

      "endDate": null,

      "startDate": 1383125910000,

      "keywords": "#storm,#ukstorm,St Jude",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "dcnavyyard",

      "name": "Washington Navy Yard incident",

      "totalCount": 239670,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1383487811000,

      "endDate": 1383742827000,

      "startDate": 1383487824000,

      "keywords": "tcot, Navy Yard ,NavyYard Navy Yard Shooting,Snowden ",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "cycloneIT-nov13",

      "name": "Cyclone-Flood Italy",

      "totalCount": 164975,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1384860120000,

      "endDate": 1384981200000,

      "startDate": 1385025543000,

      "keywords": "Italy, cyclone, Sardegna, #ForzaSardegna",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 2,

      "humanTaggedCount": null

   },

   {

      "code": "zemjotre_nov-13",

      "name": "Zemjotres",

      "totalCount": 0,

      "language": "en,mk",

      "curator": "PopovskiVasko",

      "collectionCreationDate": 1384927147000,

      "endDate": null,

      "startDate": 1384960621000,

      "keywords": "#zemjotres #makedonija",

      "geo": "0422600,0404600",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "27022010",

      "name": "Terremoto 27/2",

      "totalCount": 0,

      "language": "es",

      "curator": "stjohnsonline",

      "collectionCreationDate": 1384963387000,

      "endDate": null,

      "startDate": 1384963413000,

      "keywords": "#terremotochile",

      "geo": "-73.2965,-36.979449,-72.863372,-36.50142",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_nov-13",

      "name": "The Marriage Bill",

      "totalCount": 63160,

      "language": "en",

      "curator": "blackorwa",

      "collectionCreationDate": 1384326210000,

      "endDate": 1384506382000,

      "startDate": 1385362290000,

      "keywords": "marriage bill, matrimony bill, marriage, matrimonial property",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "yolan_0_nov-13",

      "name": "Yolanda_v1",

      "totalCount": 0,

      "language": "en",

      "curator": "CausewayDojo",

      "collectionCreationDate": 1385390766000,

      "endDate": null,

      "startDate": 1385391407000,

      "keywords": "#yolandaph #flooding",

      "geo": "113.9,3.6,132.3,25.8",

      "status": "NOT_RUNNING",

      "labelCount": 32,

      "humanTaggedCount": null

   },

   {

      "code": "tujuane_nov-13",

      "name": "tujuane",

      "totalCount": 140,

      "language": "en",

      "curator": "levisdoban",

      "collectionCreationDate": 1385545200000,

      "endDate": 1385617100000,

      "startDate": 1385551680000,

      "keywords": "#tujuane",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "prism_nsa_alex",

      "name": "PRISM/NSA scandal - Alex",

      "totalCount": 0,

      "language": "en,ru",

      "curator": "AlexMikhashchuk",

      "collectionCreationDate": 1385643559000,

      "endDate": 1385646209000,

      "startDate": 1385646174000,

      "keywords": "UK",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 24,

      "humanTaggedCount": null

   },

   {

      "code": "clex_20131201",

      "name": "Crisis Lexicon Test",

      "totalCount": 10895400,

      "language": "en",

      "curator": "ChaToX",

      "collectionCreationDate": 1385889977000,

      "endDate": 1391505972000,

      "startDate": 1391505982000,

      "keywords": "flood crisis, victims, explosion west, flood victims, flood powerful, powerful storms, hoisted flood, storms amazing, explosion, amazing rescue, rescue women, flood cost, counts flood, toll rises, braces river, river peaks, crisis deepens, explosion fertilizer, prayers, thoughts prayers, affected tornado, affected, death toll, tornado relief, photos flood, water rises, toll, flood waters, flood appeal, victims explosion, bombing suspect, massive explosion, affected areas, praying victims, injured, please join, join praying, prayers people, redcross, text redcross, visiting flood, lurches fire, video explosion, deepens death, opposed floo, help flood, worsens, west explosion, died explosions, marathon explosions, flood relief, donate, first responders, flood affected, donate cross, braces, tornado victims, deadly, prayers affected, explosions running, evacuated, relief, flood death, deaths confirmed, affected flooding, people killed, dozens, footage, survivor finds, worsens eastern, flo",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "nytraincrash",

      "name": " NY Metro-North train",

      "totalCount": 15395,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1386058438000,

      "endDate": 1401715335000,

      "startDate": 1386245460000,

      "keywords": "Metro-North, National Transportation Safety Board, NTSB, Hudson River",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "_0_dec-13",

      "name": "Mexico ",

      "totalCount": 0,

      "language": "en",

      "curator": "JisunAn",

      "collectionCreationDate": 1386226282000,

      "endDate": 1386228167000,

      "startDate": 1386226300000,

      "keywords": "mexico",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "YemenIncidentMap",

      "name": "Yemen Incident Map",

      "totalCount": 0,

      "language": "en, ar",

      "curator": "thiggins10",

      "collectionCreationDate": 1386682599000,

      "endDate": null,

      "startDate": 1386682846000,

      "keywords": "Yemen, Sanaa, Sana'a, Kidnap, AQAP",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 26,

      "humanTaggedCount": null

   },

   {

      "code": "cyclone__ind-13",

      "name": "Cyclone India 2013",

      "totalCount": 73365,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1385198817000,

      "endDate": 1387111468000,

      "startDate": 1387107955000,

      "keywords": "#India, #Cyclone, #Tropicalcyclone, #Andhrapradesh, #pondicherry, #Pithapuram, #Peddepuram, #Samalkot, #Kakinada, #Nidadavole, #Tadepallegudem, #Bhimavaram, #Narasapur, #Rajahmundry, #Kakinada, #Cocanada Bay, #Helen",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_0013-12-",

      "name": "Human Rights Abuses",

      "totalCount": 0,

      "language": "en",

      "curator": "shevy",

      "collectionCreationDate": 1387471871000,

      "endDate": 1387472010000,

      "startDate": 1387471886000,

      "keywords": "\"human rights\"",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2013-12-worldwide_earthquake_tracker",

      "name": "Worldwide earthquake tracker",

      "totalCount": 0,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1387710910000,

      "endDate": 1388674936000,

      "startDate": 1388674875000,

      "keywords": "#earthquake",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "ManilaShooting",

      "name": "Philippines shooting",

      "totalCount": 4005,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1387724431000,

      "endDate": 1387741409000,

      "startDate": 1387724593000,

      "keywords": "Philippines gun, Philippines mayor, political violence, gun shoot, gun shooting, Philippines",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "umati_dec-13",

      "name": "UMATI JANUARY.",

      "totalCount": 2591075,

      "language": "en, sw, ki",

      "curator": "faithumati",

      "collectionCreationDate": 1386052668000,

      "endDate": 1389339644000,

      "startDate": 1389335868000,

      "keywords": "kihii, jarabuon, votingrobots, madoadoa, muslims, terrorists, foreskin, okuyu, 41against1, shetani, mademini, vipii, kipii, vihii, jiggers, mpigs, mashetani, wasepere, wajaka, longsleeve, #kenyaat50, #keat50, #sickat50, #mututho, mututho, kenya@50, kenya, #kenya, #westgate, NYPD, #NYPD, uhuru, jubilee, uhunye, #KOT, #mykenya, nairobi, mombasa, kisumu, sick @ 50, sick@50, kikuyu's, kikuyu, luo, kalenjins, luos, kalenjin, nandi, somali, somalis, wasomali, walalo, manugu, maumbwa, mbwa, umbwa, maumau, #MauMau, kenyans, okuyu, kikuyus are thieves, #dupedat50, kenyatta, #kenyatta, #weareone, #weareone_Dering, harambee, #harambee, kenya at filthy, wajaka, #WeAreNotOne, #kenyaatfilthy, Pnagani, grenadeattack, Wajir, Garissa, Moyale, Nyumba Kumi, #NyumbaKumi, Ole Lenku, evil society, Kimaiyo, Kimayo, somali's, kamwana, UoN, ICC, #ICCTrials, #ICCTRilasKE, wamalize, wamalizwe, wauwawe, wauwe, waue, fagia, wafagie, waende kwao, mende, chinja, weka tyre, risasi, malaya, shoga, mashoga, wachawi, ju",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-01-test",

      "name": "test_v2",

      "totalCount": 120,

      "language": "en",

      "curator": "PatrickCostel11",

      "collectionCreationDate": 1388999971000,

      "endDate": 1395395701000,

      "startDate": 1395395455000,

      "keywords": "rihanna",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "japan_chem_explosion",

      "name": "Explosion at Japan chemical factory",

      "totalCount": 589300,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1389253942000,

      "endDate": 1390115400000,

      "startDate": 1389958866000,

      "keywords": "japan, chemical factory, explosion",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 15,

      "humanTaggedCount": null

   },

   {

      "code": "oscar2014",

      "name": "Oscar2014",

      "totalCount": 260,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1389958644000,

      "endDate": 1390115938000,

      "startDate": 1390115448000,

      "keywords": "oscar, Oscar nomination",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-01-australia_heat_wave",

      "name": "Australia Heat Wave",

      "totalCount": 17940,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1389949295000,

      "endDate": 1401958561000,

      "startDate": 1389958708000,

      "keywords": "#australia, heatwave, #tennis, heat, hot, game, playing, sunstroke, dead, heatstroke",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 24,

      "humanTaggedCount": null

   },

   {

      "code": "yolanda_children",

      "name": "Trafficking of children displaced",

      "totalCount": 675,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1390116091000,

      "endDate": 1390117278000,

      "startDate": 1390116750000,

      "keywords": "trafficking, children, Typhoon Haiyan, Samar, children displaced, Yolanda",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 27,

      "humanTaggedCount": null

   },

   {

      "code": "dolphins_slaughter",

      "name": "Dolphins slaughter",

      "totalCount": 118490,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1390117717000,

      "endDate": 1392232457000,

      "startDate": 1392196035000,

      "keywords": "japanese fishermen, dolphins slaughter, HelpCoveDolphins,  250 dolphins,Tweet4Taiji, Taiji",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 15,

      "humanTaggedCount": null

   },

   {

      "code": "2014-01-secchia_flood_2014",

      "name": "Secchia Flood 2014",

      "totalCount": 815,

      "language": "en,it",

      "curator": "danife75",

      "collectionCreationDate": 1390488088000,

      "endDate": 1390770000000,

      "startDate": 1392973402000,

      "keywords": "#allertameteoER",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 13,

      "humanTaggedCount": null

   },

   {

      "code": "2014-01-flu",

      "name": "Flu season",

      "totalCount": 99545,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1391088215000,

      "endDate": 1393785248000,

      "startDate": 1393785181000,

      "keywords": "#fluseason, flu",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-polar_vortexPolarVortex2013",

      "name": "Polar Vortex",

      "totalCount": 325,

      "language": "en",

      "curator": "HNTracker",

      "collectionCreationDate": 1391368868000,

      "endDate": 1421319609000,

      "startDate": 1421146663000,

      "keywords": "#polarvortex",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-february,04",

      "name": "february,04",

      "totalCount": 1915,

      "language": "en,it",

      "curator": "CIMASocial",

      "collectionCreationDate": 1391521182000,

      "endDate": 1391530078000,

      "startDate": 1391524511000,

      "keywords": "#allertameteo",

      "geo": "9.03,43.81,13.46,45.95",

      "status": "STOPPED",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-qcri",

      "name": "QCRI",

      "totalCount": 125,

      "language": "en",

      "curator": "ChaToX",

      "collectionCreationDate": 1392025705000,

      "endDate": 1400744567000,

      "startDate": 1395911220000,

      "keywords": "Qatar Computing Research Institute, QCRI, @ingmarweber, @patrickmeier, @jikimlucas, @chatox, @mhefeeda, @StephanVogel, @rjlipton, @richde, @mimran15, @kareemd, @asaoudi , @khaledmdiab,  ‏ @guzmanhe, @majdabbar, @qatarcomputing, @TabJnanou, @marcoserafini",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-uk_floods",

      "name": "UK Floods",

      "totalCount": 100950,

      "language": "en",

      "curator": "o_saja",

      "collectionCreationDate": 1392142848000,

      "endDate": 1394393772000,

      "startDate": 1393851959000,

      "keywords": "#thames, #UKfloods, #floods, #Rotherhithe, #Bermondsey, #somersetfloods, #Weybridge, #britainfloods, #floodaware, #UKstorms",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-storm_pax",

      "name": "Storm Pax",

      "totalCount": 15545,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1392261689000,

      "endDate": 1392386192000,

      "startDate": 1392362148000,

      "keywords": "#pax, #storm, #stormpax, #paxstorm, #icepocalypse",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-hurric_0e_sandy",

      "name": "2014_Hurricane Sandy_v1",

      "totalCount": 37985,

      "language": "en",

      "curator": "card_brittany",

      "collectionCreationDate": 1392339903000,

      "endDate": 1392325200000,

      "startDate": 1392339923000,

      "keywords": "Sandy, New York City, Manhattan, Flooding",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-tt",

      "name": "Taliban Talks",

      "totalCount": 162765,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1392396312000,

      "endDate": 1392829540000,

      "startDate": 1392829398000,

      "keywords": "Taliban, TTP, peaceTalks",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 5,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-counter_meds",

      "name": "Counter Meds",

      "totalCount": 12165,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1392659793000,

      "endDate": 1392736252000,

      "startDate": 1392659809000,

      "keywords": "tamiflu, benadryl,  robitussin, advil, kleenex, nyquil, dayquil, cough drops",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-2-BostonSnow",

      "name": "Boston Snow",

      "totalCount": 14100,

      "language": "en",

      "curator": "card_brittany",

      "collectionCreationDate": 1392669166000,

      "endDate": 1392835367000,

      "startDate": 1392829327000,

      "keywords": "Boston snow, #bosnow, Harvard Square, Cambridge, parking ban",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 8,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-ukfloods",

      "name": "ukfloods",

      "totalCount": 21825,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1392725393000,

      "endDate": 1392798737000,

      "startDate": 1392829348000,

      "keywords": "#Thames, #Seadefences,  #Sea, #Haylingisland, #Flood, ukstorm",

      "geo": "-0.634505,51.378818,-0.489407,51.496361",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "ukraine_Protester",

      "name": "Ukraine_Protester",

      "totalCount": 32885,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1392818723000,

      "endDate": 1405858224000,

      "startDate": 1405858199000,

      "keywords": "#Kyiv, #Ukrain",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 15,

      "humanTaggedCount": null

   },

   {

      "code": "2014-2-Ukraine",

      "name": "Ukraine",

      "totalCount": 567460,

      "language": "en",

      "curator": "card_brittany",

      "collectionCreationDate": 1392859589000,

      "endDate": 1393019367000,

      "startDate": 1392903427000,

      "keywords": "ukraine, independence square",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 5,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-kiev",

      "name": "Kiev",

      "totalCount": 1480,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1392925426000,

      "endDate": 1393155583000,

      "startDate": 1393148757000,

      "keywords": "Kiev",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-kiev_emerency",

      "name": "Kiev Emergency",

      "totalCount": 3137180,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1393491414000,

      "endDate": 1425895509000,

      "startDate": 1425895458000,

      "keywords": "kiev, Ukraine",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 16,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ukrainev2",

      "name": "UkraineV2",

      "totalCount": 46470,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1393700797000,

      "endDate": 1393704304000,

      "startDate": 1393701249000,

      "keywords": "ukraine, kiev, russia, putin, Ukraine, Russia, Crimea, Balaclava, Ukrainian, Putin, extremists,  extremist groups, Ukrainians, Kharkiv, Odessa, Ukrainprotests, Donetsk, Freedom, Simferopol, Viktor Yanukovych,",

      "geo": "22.14,44.39,55.92,58.15",

      "status": "NOT_RUNNING",

      "labelCount": 17,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ukrainev3",

      "name": "UkraineV3",

      "totalCount": 775965,

      "language": "English (en)",

      "curator": "card_brittany",

      "collectionCreationDate": 1393704440000,

      "endDate": 1393860888000,

      "startDate": 1393830630000,

      "keywords": "Ukraine, Russia, Crimea, Balaclava, Ukrainian, Putin, extremist groups, Ukrainians, Kharkiv, Odessa, protests, Donetsk, Freedom, Simferopol, Viktor Yanukovych, Russian Invasion, Russians, Perevalnoe, mobilised, #Sevastapol, Feodosia, Cossacks, war, Ukrainain, marines, Kiev,Sudac,Svoboda, fratricide,Dmytro Yarosh, Right Sector, paramilitary, Oleh Tyahnybok, Molotov, reservists, Muslim Crimean Tatars,Tatars, Black Sea Fleet, Black sea,  Belbek, pro-Russian activists,Obama, NATO,russia invades ukraine, EuroMaydan",

      "geo": "22.14,44.39,42.38,56.51",

      "status": "NOT_RUNNING",

      "labelCount": 74,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ukrainecrisis",

      "name": "UkraineCrisis",

      "totalCount": 126705,

      "language": "en",

      "curator": "JusMack1",

      "collectionCreationDate": 1393705739000,

      "endDate": 1393762234000,

      "startDate": 1393712015000,

      "keywords": "#Ukraine, #Russia, #Crimea, #Balaclava, #Ukrainian, #Putin, #extremist groups, #Ukrainians, #Kharkiv, #Odessa, #protests, #Donetsk, #Freedom, #Simferopol, #Viktor Yanukovych, #Russian Invasion,# Russians,#Shooting, #shot, #Fighting, #Fight, #Bombing, #bomb, #Bio Chemical, #chemical,#Death,#Injury, #dead, #rape, #torture, #Kidnap,#Displacement,#Location, #Checkpoint,# Medical unit, #hospital,\n#Deployment of arms, #arms, #guns, #gun,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 46,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ukrainev4",

      "name": "UkraineV4",

      "totalCount": 84705,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1393861023000,

      "endDate": 1393864828000,

      "startDate": 1393861050000,

      "keywords": "Ukraine, Russia, Crimea, Balaclava, Ukrainian, Putin, extremist groups, Ukrainians, Kharkiv, Odessa, protests, Donetsk, Freedom, Simferopol, Viktor Yanukovych, Russian Invasion, Russians, Perevalnoe, mobilised, #Sevastapol, Feodosia, Cossacks, war, Ukrainain, marines, Kiev,Sudac,Svoboda, fratricide,Dmytro Yarosh, Right Sector, paramilitary, Oleh Tyahnybok, Molotov, reservists, Muslim Crimean Tatars,Tatars, Black Sea Fleet, Black sea, Belbek, pro-Russian activists,Obama, NATO,russia invades ukraine, EuroMaydan",

      "geo": "22.14,44.39,40.23,52.38",

      "status": "STOPPED",

      "labelCount": 75,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ukrainev5",

      "name": "UkraineV5",

      "totalCount": 3295485,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1393864909000,

      "endDate": 1394122011000,

      "startDate": 1393865254000,

      "keywords": "Ukraine, Russia, Crimea, Balaclava, Ukrainian, Putin, extremist groups, Ukrainians, Kharkiv, Odessa, protests, Donetsk, Freedom, Simferopol, Viktor Yanukovych, Russian Invasion, Russians, Perevalnoe, mobilised, #Sevastapol, Feodosia, Cossacks, war, Ukrainain, marines, Kiev,Sudac,Svoboda, fratricide,Dmytro Yarosh, Right Sector, paramilitary, Oleh Tyahnybok, Molotov, reservists, Muslim Crimean Tatars,Tatars, Black Sea Fleet, Black sea, Belbek, pro-Russian activists,Obama, NATO,russia invades ukraine, EuroMaydan",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 8,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-shooting",

      "name": "shooting",

      "totalCount": 45,

      "language": "en",

      "curator": "techopsol1",

      "collectionCreationDate": 1394020026000,

      "endDate": 1394020306000,

      "startDate": 1394020191000,

      "keywords": "shooting, gunman, gunmen, shooter",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-traffic",

      "name": "Traffic",

      "totalCount": 93150,

      "language": "",

      "curator": "ingmarweber",

      "collectionCreationDate": 1393936373000,

      "endDate": 1395308877000,

      "startDate": 1394547560000,

      "keywords": "",

      "geo": "50.68,24.6106,51.9557,26.2647",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_0014-03-",

      "name": "Ukraine health 1",

      "totalCount": 2996680,

      "language": "en",

      "curator": "BertBrugghemans",

      "collectionCreationDate": 1394122268000,

      "endDate": 1394538611000,

      "startDate": 1394122301000,

      "keywords": "Ukraine, Russia, Crimea, Balaclava, Ukrainian, Putin, extremist groups, Ukrainians, Kharkiv, Odessa, protests, Donetsk, Freedom, Simferopol, Viktor Yanukovych, Russian Invasion, Russians, Perevalnoe, mobilised, #Sevastapol, Feodosia, Cossacks, war, Ukrainain, marines, Kiev,Sudac,Svoboda, fratricide,Dmytro Yarosh, Right Sector, paramilitary, Oleh Tyahnybok, Molotov, reservists, Muslim Crimean Tatars,Tatars, Black Sea Fleet, Black sea, Belbek, pro-Russian activists,Obama, NATO,russia invades ukraine, EuroMaydan",

      "geo": "22.14,44.18,40.23,52.38",

      "status": "STOPPED",

      "labelCount": 25,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-mh370",

      "name": "Malaysia Airlines flight #MH370",

      "totalCount": 3815325,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1394537405000,

      "endDate": 1405113907000,

      "startDate": 1404931252000,

      "keywords": "Malaysia Airlines flight MH370,  #MH370, #PrayForMH370, #MalaysiaAirlines",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-nyc_explosion",

      "name": "NYC Explosion",

      "totalCount": 365865,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1394642732000,

      "endDate": 1394955377000,

      "startDate": 1394642873000,

      "keywords": "EastHarlem, NYC, NYC emergency, NYC EXPLOSION",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-digital_disease_detection",

      "name": "Digital Disease Detection_v1",

      "totalCount": 1105,

      "language": "en",

      "curator": "kasshout",

      "collectionCreationDate": 1394944275000,

      "endDate": 1394944925000,

      "startDate": 1394944793000,

      "keywords": "#DigDisDet",

      "geo": "87.69,-10.33,134.07,38.89",

      "status": "STOPPED",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-sony",

      "name": "Sony",

      "totalCount": 995,

      "language": "en",

      "curator": "StephanVogel",

      "collectionCreationDate": 1394971970000,

      "endDate": 1394977865000,

      "startDate": 1394972053000,

      "keywords": "Sony, robotics",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-los_angeles_earthquake",

      "name": "Los Angeles Earthquake",

      "totalCount": 17790,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1395129937000,

      "endDate": 1395308772000,

      "startDate": 1395131465000,

      "keywords": "LA earthquake, 4.4 magnitude quake, San Fernando Valley, #earthquake, LA quake",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-russian",

      "name": "Russian Sanction",

      "totalCount": 406245,

      "language": "English (en), EL",

      "curator": "jikimlucas",

      "collectionCreationDate": 1395131343000,

      "endDate": 1398590762000,

      "startDate": 1398590756000,

      "keywords": "putin, Russian officials, diplomatic isolation, asset freeze",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 9,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-canada_storm_2014",

      "name": "Canada Storm 2014",

      "totalCount": 2368415,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1395827016000,

      "endDate": 1396047600000,

      "startDate": 1395829713000,

      "keywords": "PrinceEdwardIsland, Nova Scotia, Nova, Scotia, Halifax, Canadastorm, Canada, Storm, Maritimes, Canadasnow, citywx, calgary, altstorm, atlstorm, blizzard, #NL, #NS, #PEI, #NB, #QC, #TWN, stormsurge, surge, St. John's,\tNewfoundland, Labrador, Moncton, Saint John, Cape Breton, Sydney, Glace Bay, Sydney Mines, Fredericton, Charlottetown, Truro, New Glasgow, Bathurst, Miramichi, Corner Brook, Kentville,\tNova Scotia, Edmundston, New Brunswick, Summerside, Prince Edward Island, Grand Falls-Windsor, Gander,Newfoundland, Labrador, Bay Roberts",

      "geo": "-141.0,41.7,-52.6,83.1",

      "status": "NOT_RUNNING",

      "labelCount": 37,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-antwerp_riots",

      "name": "Antwerp Riots",

      "totalCount": 2335,

      "language": "nl",

      "curator": "BertBrugghemans",

      "collectionCreationDate": 1395833957000,

      "endDate": 1396000919000,

      "startDate": 1396000907000,

      "keywords": "foorkramers, foorkramer, foorwijf, fooraap, antwerpen",

      "geo": "2.3889,50.6463,6.408,51.5517",

      "status": "NOT_RUNNING",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-traffic_accidents_belgium",

      "name": "Traffic Accidents Belgium",

      "totalCount": 37915,

      "language": "nl",

      "curator": "BertBrugghemans",

      "collectionCreationDate": 1395833826000,

      "endDate": 1396000899000,

      "startDate": 1396000925000,

      "keywords": "crash, botsing, accident, boem, ongeval, brand, kapot, autostrade, autosnelweg, gewond, gekwetst",

      "geo": "2.39,50.7,6.41,51.55",

      "status": "NOT_RUNNING",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-pandemie",

      "name": "Pandemie",

      "totalCount": 300,

      "language": "de",

      "curator": "Admiral_Ackbar7",

      "collectionCreationDate": 1396339036000,

      "endDate": 1396340083000,

      "startDate": 1396339521000,

      "keywords": "Pandemie, Katastrophe, Panik",

      "geo": "5.96,47.68,15.23,54.15",

      "status": "STOPPED",

      "labelCount": 16,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-",

      "name": "Hoeneß",

      "totalCount": 225,

      "language": "de",

      "curator": "Admiral_Ackbar7",

      "collectionCreationDate": 1396337995000,

      "endDate": 1396340111000,

      "startDate": 1396340089000,

      "keywords": "#Hoeneß, #FCBayern",

      "geo": "5.96,45.82,15.23,54.15",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-flood",

      "name": "Flut",

      "totalCount": 62855,

      "language": "de",

      "curator": "Admiral_Ackbar7",

      "collectionCreationDate": 1396337698000,

      "endDate": 1397142002000,

      "startDate": 1396966255000,

      "keywords": "flut, Überschwemmung, #Flut, #Überschwemmung",

      "geo": "5.96,47.68,15.23,54.15",

      "status": "STOPPED",

      "labelCount": 17,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-chile_earthquake_2014",

      "name": "Chile Earthquake 2014",

      "totalCount": 318375,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1396406359000,

      "endDate": 1397843360000,

      "startDate": 1397751962000,

      "keywords": "#EarthquakeInChile, #ChileEarthquake, #chiletsunami, earthquake pisagua, earthquake antofagasta, earthquake arica, earthquake patache, earthquake iquique, earthquake chile, tsunami chile, tsunami pisagua, tsunami antofagasta, tsunami arica, tsunami patache, tsunami iquique,  #prayforarica, #prayforchile, #prayforiquique, #prayforpisagua, #prayforantofagasta",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-terremoto_chile_2014",

      "name": "Terremoto Chile 2014",

      "totalCount": 658870,

      "language": "es",

      "curator": "ChaToX",

      "collectionCreationDate": 1396412175000,

      "endDate": 1397395103000,

      "startDate": 1397397503000,

      "keywords": "#fuerzachile, #fuerzaarica, #fuerzaiquique, #fuerzaantofagasta, #fuerzanortedechile, #yoestoyconchile, #yoestoyconarica, #yoestoyconiquique, #yoestoyconantofagasta, #terremotoenchile, #terremotochile, #terremotoenarica, #terremotoarica, #terremotoeniquique, #terremotoiquique, #terremotoenantofagasta, #terremotoantofagasta, chile terremoto, arica terremoto, iquique terremoto, antofagasta terremoto, chile tsunami, arica tsunami, iquique tsunami, antofagasta tsunami, #chiletsunami, #yoestoyconarica, chile maremoto, arica maremoto, iquique maremoto, #chileterremoto, terremoto iqq, tsunami iqq, réplica chile, réplica iquique, réplica antofagasta, réplica arica, temblor chile, temblor iquique, temblor antofagasta, temblor arica, #Antofagasta, #Arica, #Iquique, #Tocopilla, terremoto alto hospicio, temblor alto hospicio, réplica alto hospicio, terremoto parinacota, temblor parinacota, réplica parinacota",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-save_kessab_case",

      "name": "Save Kessab case",

      "totalCount": 995,

      "language": "en,",

      "curator": "Kornelij",

      "collectionCreationDate": 1396522080000,

      "endDate": 1397191645000,

      "startDate": 1396966382000,

      "keywords": "#SaveKessab #Kessab",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-umatilla",

      "name": "umatilla",

      "totalCount": 0,

      "language": "en",

      "curator": "ToxTrack",

      "collectionCreationDate": 1396477335000,

      "endDate": 1396541541000,

      "startDate": 1396541455000,

      "keywords": "umatilla explosion",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-plymouth_ng_explosion",

      "name": "Plymouth NG Explosion",

      "totalCount": 1485,

      "language": "en",

      "curator": "ToxTrack",

      "collectionCreationDate": 1396477151000,

      "endDate": 1396546006000,

      "startDate": 1396541597000,

      "keywords": "#plymouth, #umatilla, #explosion, #natgas",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-fort_hood",

      "name": "Fort Hood",

      "totalCount": 31370,

      "language": "en",

      "curator": "ToxTrack",

      "collectionCreationDate": 1396545981000,

      "endDate": 1396735277000,

      "startDate": 1396559698000,

      "keywords": "fort hood shooting",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-hunger_test_1",

      "name": "Hunger test 1",

      "totalCount": 75985,

      "language": "en",

      "curator": "hungertech",

      "collectionCreationDate": 1396557609000,

      "endDate": 1396735292000,

      "startDate": 1396559554000,

      "keywords": "hunger, foodsecurity, foodprices",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 13,

      "humanTaggedCount": null

   },

   {

      "code": "cyber_attack_opisrael",

      "name": "Cyber Attacks",

      "totalCount": 18065,

      "language": "en",

      "curator": "dimleyk",

      "collectionCreationDate": 1396855847000,

      "endDate": 1397495982000,

      "startDate": 1397467944000,

      "keywords": "#OPISRAEL, #OpIsraelBirthday, #defaced, #hacked, #AnonGhost",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 13,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-measles",

      "name": "Measles",

      "totalCount": 701415,

      "language": "en",

      "curator": "kerenkeren",

      "collectionCreationDate": 1396905444000,

      "endDate": 1397495971000,

      "startDate": 1397468065000,

      "keywords": "measles, measle, #measles, #themeasles",

      "geo": "-74.278314,40.495996,-73.700272,40.836338",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-test",

      "name": "test23_test",

      "totalCount": 5345,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1397094803000,

      "endDate": 1398670398000,

      "startDate": 1398670379000,

      "keywords": "uk",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-athen",

      "name": "Athen 2014-10-04",

      "totalCount": 7845,

      "language": "en",

      "curator": "Stefan_Martini",

      "collectionCreationDate": 1397119256000,

      "endDate": 1397379602000,

      "startDate": 1397119270000,

      "keywords": "#athen #explosion #bomb",

      "geo": "23.567984,37.824149,23.887984,38.144149",

      "status": "STOPPED",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "SolomonEQ",

      "name": "Solomon Islands EQ April 12, 2014",

      "totalCount": 0,

      "language": "en",

      "curator": "wharman",

      "collectionCreationDate": 1397335843000,

      "endDate": 1397395003000,

      "startDate": 1397397541000,

      "keywords": "#SolomonIslands #earthquake #tsunami #AmericanSamoa",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-incendio_en_valparaiso",

      "name": "Incendio en Valparaiso",

      "totalCount": 72995,

      "language": "en,es",

      "curator": "reparo2008",

      "collectionCreationDate": 1397569986000,

      "endDate": 1398243607000,

      "startDate": 1397981949000,

      "keywords": "#incendiovalpo,#FuerzaValpo, incendio valpo, incendio valparaiso, victimas valparaiso",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-boston_marathon",

      "name": "Boston Marathon",

      "totalCount": 264905,

      "language": "en",

      "curator": "card_brittany",

      "collectionCreationDate": 1397607748000,

      "endDate": 1397653200000,

      "startDate": 1397607777000,

      "keywords": "boston, marathon, #bostonstrong, finish line, stronger, #wewillrun, running",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 5,

      "humanTaggedCount": null

   },

   {

      "code": "NOLA-test",

      "name": "Strictly Spatial",

      "totalCount": 398830,

      "language": "en",

      "curator": "Pothole_Report",

      "collectionCreationDate": 1397827664000,

      "endDate": 1405965600000,

      "startDate": 1401984649000,

      "keywords": "homicide, domesticviolence, sgbv, rape, #nola, #neworleans, violence, new orleans, missing person, human trafficking, femicide, domestic homicide, domestic violence, intimate partner violence, legislation, policy, advocacy, human rights, child sex work, social services, criminal justice, police, urban violence, political violence, gang violence, gang rape, abduction,",

      "geo": "-134.3,81.5,-135.4,-71.0",

      "status": "STOPPED",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-south_korea_ferry",

      "name": "South Korea Ferry",

      "totalCount": 60785,

      "language": "en",

      "curator": "gurlinthewurld",

      "collectionCreationDate": 1397827646000,

      "endDate": 1398243613000,

      "startDate": 1397981911000,

      "keywords": "#ferry, #korea, #location",

      "geo": "123.7,33.91,131.49,39.65",

      "status": "STOPPED",

      "labelCount": 2,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-ferry_sk",

      "name": "Ferry SK",

      "totalCount": 75,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1397827645000,

      "endDate": 1397828403000,

      "startDate": 1397827685000,

      "keywords": "#ferry, #korea, #location,",

      "geo": "124.61,33.11,130.92,38.62",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-mexico_earthquake",

      "name": "Mexico Earthquake",

      "totalCount": 0,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1397843767000,

      "endDate": 1397976691000,

      "startDate": 1397843846000,

      "keywords": "mexico, #mexico, earthquake,",

      "geo": "118.36,14.53,-86.71,32.72",

      "status": "STOPPED",

      "labelCount": 18,

      "humanTaggedCount": null

   },

   {

      "code": "TB1",

      "name": "TestTB1",

      "totalCount": 95105,

      "language": "en",

      "curator": "timothieb",

      "collectionCreationDate": 1398113026000,

      "endDate": 1398188648000,

      "startDate": 1398113057000,

      "keywords": "#gas",

      "geo": "-73.94598,40.628501,-73.722144,40.792088",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-mers",

      "name": "Middle East Respiratory Syndrome",

      "totalCount": 313685,

      "language": "en",

      "curator": "ChaToX",

      "collectionCreationDate": 1398601784000,

      "endDate": 1405362697000,

      "startDate": 1405353119000,

      "keywords": "coronavirus, corona virus, mers avian flu, mers camel, mers case, mers cases, mers contact, mers concerned, mers concerns, mers cov, mers-cov, mers deadly, mers death, mers disease, mers drugs, mers epidemic, mers genome, mers h7n9, mers haj, mers health, mers healthcare, mers hospital, mers infection, mers infects, mers medicine, mers outbreak, mers paper, mers patient, mers patients, mers precaution, mers precautionary, mers research, mers samples, mers saudi, mers saudi arabia, mers science, mers severe, mers sick, mers spread, mers spreads, mers spreading, mers studies, mers syndrome, mers toll, mers vaccine, mers vaccines, mers virus, mers worry, mers worries, middle eastern sars, middle eastern virus, middle east respiratory syndrome, middle east respiratory virus, middle east virus, sars like virus, sars-like virus",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-central_u.s_tornados",

      "name": "Central U.S Tornados",

      "totalCount": 1253435,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1398605006000,

      "endDate": 1398657849000,

      "startDate": 1398657834000,

      "keywords": "tornado, Beaufort County, Arkansas, Louisiana, Texas, North Carolina, Great Plains, storm, hail, flood,",

      "geo": "-114.38,25.16,-84.1,46.57",

      "status": "STOPPED",

      "labelCount": 18,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-tornado_28th_april_2014",

      "name": "Tornado 28th April 2014",

      "totalCount": 2810970,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1398657899000,

      "endDate": 1398960000000,

      "startDate": 1398787063000,

      "keywords": "#tornado, Beaufort County, #Arkansas, #Louisiana, Texas, North Carolina, Great Plains, #storm, #hail, #flood,#Baxter Springs,#Mayflower,\n#Edenton, #NorthCarolina, #Hume, #Nevada, #plantation,#RedCross, #paramedics, #firefighter, #Doctor, #Help, #alabama, #mississippi, #tupelo",

      "geo": "-98.91,27.89,-78.19,41.14",

      "status": "STOPPED",

      "labelCount": 23,

      "humanTaggedCount": null

   },

   {

      "code": "BringBackOurGirls",

      "name": "Kidnapped Nigerian girls",

      "totalCount": 1322535,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1399227819000,

      "endDate": 1405858524000,

      "startDate": 1405858500000,

      "keywords": "#BringBackOurGirls, #BBOG, #NigerianGirls, nigerian missing girls, nigerian abducted girls, nigerian kidnap girls, nigerian kidnapped girls, nigerian missing schoolgirls, nigerian abducted schoolgirls, nigerian kidnap schoolgirls, nigerian kidnapped schoolgirls, nigeria missing girls, nigeria abducted girls, nigeria kidnap girls, nigeria kidnapped girls, nigeria missing schoolgirls, nigeria abducted schoolgirls, nigeria kidnap schoolgirls, nigeria kidnapped schoolgirls, boko haram missing girls, boko haram abducted girls, boko haram kidnap girls, boko haram kidnapped girls, boko haram missing schoolgirls, boko haram abducted schoolgirls, boko haram kidnap schoolgirls, boko haram kidnapped schoolgirls, bokoharam missing girls, bokoharam abducted girls, bokoharam kidnap girls, bokoharam kidnapped girls, bokoharam missing schoolgirls, bokoharam abducted schoolgirls, bokoharam kidnap schoolgirls, bokoharam kidnapped schoolgirls, chibok girls, chibok schoolgirls, chibok students",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 20,

      "humanTaggedCount": null

   },

   {

      "code": "SyriaMay2014",

      "name": "Syria crisis",

      "totalCount": 33130,

      "language": "en,",

      "curator": "LattimerC",

      "collectionCreationDate": 1400140256000,

      "endDate": 1400313601000,

      "startDate": 1400140290000,

      "keywords": "#syria, #syriacrisis, #syriaemergency, #syriahumanitarian",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-serbiabosniafloods",

      "name": "serbiabosniafloods",

      "totalCount": 49190,

      "language": "en",

      "curator": "BertBrugghemans",

      "collectionCreationDate": 1400254701000,

      "endDate": 1400270799000,

      "startDate": 1400270502000,

      "keywords": "#serbiafloods, #bosniafloods",

      "geo": "15.17,41.71,23.01,46.19",

      "status": "STOPPED",

      "labelCount": 30,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-bosniaserbiafloods",

      "name": "BosniaSerbiaFloods",

      "totalCount": 3010,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1400263654000,

      "endDate": 1400266177000,

      "startDate": 1400266016000,

      "keywords": "serbiafloods, bosniafloods, poplave, poplava, floods, Serbia, Bosnia, Obrenovac, beogradnavodi, Doboj, Maglaj, pomoc, vrsac, srbija, pomoć, BiH, #serbiafloods, #bosniafloods, #poplave, #poplava, #floods, #Serbia, #Bosnia, #Obrenovac, #beogradnavodi, #Doboj, #Maglaj, #pomoc, #vrsac, #srbija, #pomoć, #BiH, #poplaveBiH, #landslides, #potop, #Balkan, #SerbiaFloods, #SerbianFloods, #Svilajnac, flood, water, food, dead, injured, missing",

      "geo": "18.84,41.07,25.34,46.19",

      "status": "STOPPED",

      "labelCount": 13,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-balkanfloods",

      "name": "balkanfloods",

      "totalCount": 360740,

      "language": "en",

      "curator": "BertBrugghemans",

      "collectionCreationDate": 1400270783000,

      "endDate": 1401375602000,

      "startDate": 1400767761000,

      "keywords": "serbianfloods, bosniafloods, serbiafloods, serbiaflood, bosniaflood, bosnia, serbia,",

      "geo": "15.02,41.51,25.27,46.19",

      "status": "TRASHED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-serbia_bosnia_floods_may_2014",

      "name": "Serbia-Bosnia Floods May 2014",

      "totalCount": 298465,

      "language": "en,",

      "curator": "IrinaTemnikova",

      "collectionCreationDate": 1400428558000,

      "endDate": 1401375615000,

      "startDate": 1400767745000,

      "keywords": "Obrenovac, #SerbiaFloods, Serbia, #SerbiaNeedsHelp, poplava, poplave, poplave2014, #SerbiaFloodRelief, Svilajnac, Posavina, \"River Sava\", #HumanostNaDelu, Obrenovac, Kolubara, SRBIJA, Bosnia, #RepublikaSrpska, Свилајнац, поплаве, поплава, Србија, Обреновац, Обреновцу, #BosnianFloods, #Balkanfloods, #savamala, #Poplave2014",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 22,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-election_india_2014",

      "name": "Election India 2014",

      "totalCount": 37785,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1399215022000,

      "endDate": 1400762785000,

      "startDate": 1400755966000,

      "keywords": "election india, lok sabha, india vote, bjp, congress, aap, modi, kejriwal, rahul, gandhi",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-election_india_new_government",

      "name": "Election India New Government",

      "totalCount": 166555,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1400764750000,

      "endDate": 1402887601000,

      "startDate": 1402842424000,

      "keywords": "election results, bjp, nda government, modi, opposition, ministry, parliament",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-emsc_landslides_2014",

      "name": "EMSC Landslides by KW eng",

      "totalCount": 1032655,

      "language": "en",

      "curator": "ManzanaMecanica",

      "collectionCreationDate": 1401290098000,

      "endDate": 1445920579000,

      "startDate": 1446122543000,

      "keywords": "avalanche affected, avalanche alert, avalanche buried, avalanche buries, avalanche bury, avalanche casualties, avalanche collapse, avalanche collapses, avalanche dead, avalanche debris, avalanche die, avalanche disaster, avalanche earthquake, avalanche evacuated, avalanche fatal, avalanche flood, avalanche floods, avalanche ground, avalanche hill, avalanche hills, avalanche home, avalanche homes, avalanche houses, avalanche ice, avalanche injured, avalanche kill, avalanche kills, avalanche lake, avalanche missing, avalanche mountain, avalanche mud, avalanche rain, avalanche repair, avalanche repairs, avalanche rescue, avalanche risk, avalanche river, avalanche road, avalanche roadblock, avalanche rock, avalanche rocks, avalanche search, avalanche slab, avalanche slope, avalanche snow, avalanche snowfall, avalanche snowpack, avalanche storm, avalanche storms, avalanche survives, avalanche survivor, mudslide -cream -icecream, rock fall -boy, rockslide, snowslide, snowslip, avalanche emergency, avalanche witness, avalanche height, avalanche scarp, avalanche deposits",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-Tierras",

      "name": "Tierras",

      "totalCount": 98875,

      "language": "es",

      "curator": "kublaykan",

      "collectionCreationDate": 1402273583000,

      "endDate": 1402328489000,

      "startDate": 1402277514000,

      "keywords": "restitucion, tierras, despojo, victimas,",

      "geo": "-80.77,-4.45,-65.62,13.37",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "bergdahl_for_taliban",

      "name": "Bergdahl for Taliban",

      "totalCount": 7230,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1402311387000,

      "endDate": 1402736408000,

      "startDate": 1402561757000,

      "keywords": "Bergdahl for Taliban,  five Taliban, Taliban for U.S. soldier",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 25,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-elecciones_colombia_2014",

      "name": "Elecciones Colombia 2014",

      "totalCount": 1833220,

      "language": "es",

      "curator": "kublaykan",

      "collectionCreationDate": 1402276903000,

      "endDate": 1402560054000,

      "startDate": 1402914447000,

      "keywords": "Elecciones, Colombia, 2014, Juan Manuel, Santos, Oscar Ivan, Zuluaga, registraduria,",

      "geo": "-80.77,-4.45,-65.62,13.37",

      "status": "NOT_RUNNING",

      "labelCount": 19,

      "humanTaggedCount": null

   },

   {

      "code": "HQprices2014",

      "name": "HQ prices",

      "totalCount": 0,

      "language": "en",

      "curator": "HQprice",

      "collectionCreationDate": 1402390218000,

      "endDate": 1402394401000,

      "startDate": 1402394388000,

      "keywords": "HQprice, EUR",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-testtrashkoushik",

      "name": "testTrashKoushik",

      "totalCount": 20,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1402393577000,

      "endDate": 1405515600000,

      "startDate": 1402393594000,

      "keywords": "cyclone, bay of bengal, monsoon, coastal, orissa, andhra, bangladesh",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "Prices2014",

      "name": "Prices",

      "totalCount": 0,

      "language": "en",

      "curator": "HQprice",

      "collectionCreationDate": 1402394467000,

      "endDate": 1402395880000,

      "startDate": 1402395492000,

      "keywords": "@HQprice",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "HQprices2",

      "name": "HQ prices 2",

      "totalCount": 0,

      "language": "en",

      "curator": "HQprice",

      "collectionCreationDate": 1402399864000,

      "endDate": 1402561291000,

      "startDate": 1402560152000,

      "keywords": "@HQprice",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-langer_tisch_wuppertal",

      "name": "Langer Tisch Wuppertal",

      "totalCount": 125,

      "language": "de",

      "curator": "BukTracker",

      "collectionCreationDate": 1402855627000,

      "endDate": 1402951193000,

      "startDate": 1402914424000,

      "keywords": "",

      "geo": "7.095285,51.220081,7.280359,51.301386",

      "status": "STOPPED",

      "labelCount": 2,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-wcbr",

      "name": "WCBR",

      "totalCount": 165,

      "language": "en,,pt",

      "curator": "theoscarito",

      "collectionCreationDate": 1402927075000,

      "endDate": 1405126807000,

      "startDate": 1404951058000,

      "keywords": "#copapraquem, #occupyworldcup, #fifagohome, #naovaitercopa, #FuckFIFA, #ocupacopa, #vemprarua, #BlackBloc, #opMundial2014,  #opWorldCup, #NaoWorldCup",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "Sturm2014",

      "name": "Sturm Rheinland Juni 2014",

      "totalCount": 0,

      "language": "de",

      "curator": "BukTracker",

      "collectionCreationDate": 1402951176000,

      "endDate": null,

      "startDate": 1403010141000,

      "keywords": "unwetter, sturm, Duesseldorf, Düsseldorf, Essen, unwetter,",

      "geo": "6.4164,50.6667,7.4288,51.5752",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-emsc_landslides_2014",

      "name": "JI emsc landslides 2014",

      "totalCount": 190,

      "language": "en,fr,it,es",

      "curator": "jikimlucas",

      "collectionCreationDate": 1404911459000,

      "endDate": 1415093147000,

      "startDate": 1415092172000,

      "keywords": "landslide, avalancha, crisis",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-wateroverlast",

      "name": "Wateroverlast",

      "totalCount": 0,

      "language": "nl",

      "curator": "NoodNetwerk",

      "collectionCreationDate": 1405068449000,

      "endDate": 1405335473000,

      "startDate": 1405068900000,

      "keywords": "#wateroverlast,#overstroming,#dijkdoorbraak,#noodweer,#storm,#nooddijk,#watersnood,#hoogwater,#stormvloed,#waterkering,#stormvloedkering",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-fifa_2015",

      "name": "FIFA 2014 WorldCup",

      "totalCount": 7496270,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1402048334000,

      "endDate": 1405529065000,

      "startDate": 1405950592000,

      "keywords": "#FIFA2014, #WorldCup, #Brazil2014, #naovaitercopa, #DilmaRoussef, #AgenciaPublica, #WorldCupForWhom, #NaoVaiTerCopa, #TherewillbenoWorldCup, #VaiTerCopa, #CopaDoMundo, #copapraquem, #occupyworldcup, #fifagohome",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-belfast_trouble",

      "name": "dd",

      "totalCount": 17475,

      "language": "en",

      "curator": "penfoldxxl",

      "collectionCreationDate": 1405026040000,

      "endDate": 1405026000000,

      "startDate": 1405109823000,

      "keywords": "",

      "geo": "-6.3707,54.3349,-5.3832,54.8922",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-possible_belfast_disturbance_12th_july_2",

      "name": "Possible Belfast Disturbance 12th July 2",

      "totalCount": 750,

      "language": "en",

      "curator": "penfoldxxl",

      "collectionCreationDate": 1405110055000,

      "endDate": 1405310411000,

      "startDate": 1405180571000,

      "keywords": "#camptwaddell ,#twadell, #protest, #riot, #trouble, #marching, #orangemen, #Ardoyne, #shop , #fronts, #Crumlin Road, #Crumlin, #PSNI, #police, #twelfth, #twelfth 2014, #march, #King Billy, #Battle, #Boyne, #Orangefest, #Hesketh, #Woodvale, #Orange order,#orange hall, #petrol, #shots, #baton, #rounds, #injured, #graduated response, #chapel, #Sinn Fein, #water cannon, #unionist, #nationalist",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 35,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-japan_6.8-magnitude_earthquake",

      "name": "Japan 6.8-magnitude earthquake",

      "totalCount": 0,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1405113821000,

      "endDate": 1405330170000,

      "startDate": 1405117660000,

      "keywords": "6.8-magnitude earthquake, tsunami, Fukushima",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-palestine_conflict",

      "name": "Palestine Conflict",

      "totalCount": 23628785,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1405113974000,

      "endDate": 1412301217000,

      "startDate": 1412243894000,

      "keywords": "#GazaUnderAttack, #SaveGaza, #PrayForGaza, Palestine, Gaza,  #FreePalestine, #ProIsrael, #IsraelUnderFire, #IsraelUnderAttack, #IStandWithIsrael, #SaveGazaFromHamas, #Hamaz, #SOSPalestine, #WhyIsrael, #ICC4Israel, #gazachildren, #SupportGaza, #warcrimes, #IsraelKillsKids, #ProtectiveEdge, #Hamas, #EyalGiladNaftali, #FreeGazaFromHamas, #ISupportIsrael, #IStandWithIsrael, #PrayForIsrael, #IsraelUnderFire, #HamasTerrorists, #GodBlessTheIDF, #IDF",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-rammasun",

      "name": "Rammasun",

      "totalCount": 261565,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1405334625000,

      "endDate": 1405976400000,

      "startDate": 1406121110000,

      "keywords": "rammasun, glendaph, typhoonrammasun, typhoonglenda, glenda hurricane, glenda storm, glenda typhoon, philippines hurricane, philippines storm, philippines typhoon, manila hurricane, manila storm, manila typhoon, leyte hurricane, leyte storm, leyte typhoon, catarman hurricane, catarman storm, catarman typhoon, northern samar hurricane, northern samar storm, northern samar typhoon, albay-sorsogon hurricane, albay-sorsogon storm, albay-sorsogon typhoon, southern tagalog hurricane, southern tagalog storm, southern tagalog typhoon, quezon city hurricane, quezon city storm, quezon city typhoon, guagua hurricane, guagua storm, guagua typhoon, central luzon hurricane, central luzon storm, central luzon typhoon, cavite city hurricane, cavite city storm, cavite city typhoon, binangonan hurricane, binangonan storm, binangonan typhoon, calamba hurricane, calamba storm, calamba typhoon",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 38,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-mascrash",

      "name": "MAS Crash",

      "totalCount": 1236270,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1405612491000,

      "endDate": 1405714847000,

      "startDate": 1405704141000,

      "keywords": "MAS crash, MH17, Plane crash, Malaysian Airlines plane crash",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-malaysia_airlines_crash_ukraine",

      "name": "Malaysia Airlines Crash Ukraine",

      "totalCount": 208210,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1405615288000,

      "endDate": 1406058939000,

      "startDate": 1406121419000,

      "keywords": "#MH17, Malaysia Airlines flight MH17, Malaysian Airlines Boeing 777, plane crash, airplane crash, Donetsk, 777, #Ukraine, #PrayersForMalaysia, #PrayForMH17, Amsterdam to Kuala Lumpur, Easter Ukraine, 295 people",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-flight_mh17_victims",

      "name": "Flight MH17 Victims",

      "totalCount": 346030,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1405877584000,

      "endDate": 1406313552000,

      "startDate": 1406203050000,

      "keywords": "#OhMy #Anticipate, MH17, Crash Victims",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-fire_jack_hill",

      "name": "Fire Jack hill",

      "totalCount": 30,

      "language": "en",

      "curator": "Mark_Codling",

      "collectionCreationDate": 1406528222000,

      "endDate": 1407031753000,

      "startDate": 1407031276000,

      "keywords": "#jackshill #jamaica",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-gazajothi",

      "name": "GazaJothi",

      "totalCount": 75025,

      "language": "en",

      "curator": "Jothirnadh",

      "collectionCreationDate": 1407753524000,

      "endDate": 1412982001000,

      "startDate": 1412936617000,

      "keywords": "#Gaza,#GazaStrip,#Gaza crisis,#Palestinian refugees",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-india_flood",

      "name": "India Flood",

      "totalCount": 5200680,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1407686065000,

      "endDate": 1409730646000,

      "startDate": 1409730446000,

      "keywords": "odissa, flood, flooding, bihar, odisha, nepal, kathmandu, landslide, up, uttar pradesh, sikkim, kathmandu, nepal aid, india aid",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-california_earthquake_2014",

      "name": "California Earthquake 2014",

      "totalCount": 253940,

      "language": "en",

      "curator": "ChaToX",

      "collectionCreationDate": 1408896540000,

      "endDate": 1409392510000,

      "startDate": 1409392247000,

      "keywords": "napa earthquake, sonoma earthquake, bay area earthquake, california earthquake, ca earthquake, sfearthquake, san francisco earthquake, napaearthquake, sfquake, napaquake, napa quake, sonoma quake, bay area quake, california quake, ca quake, san francisco quake",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "Icevolcano2014",

      "name": "Iceland Volcano",

      "totalCount": 83115,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1408895234000,

      "endDate": 1409569200000,

      "startDate": 1409393077000,

      "keywords": "icelandvolcano, Vattnajokull, Bardarbunga, Bárðarbunga, volcano earthquake, volcano quake, iceland warning, iceland danger, iceland emergency, iceland alert, iceland volcano, iceland volcanoes, iceland eruption, iceland lava, iceland smoke, iceland ash, iceland ashes, iceland tectonic, iceland tectonics, iceland evacuation, iceland evacuates, icelandic warning, icelandic danger, icelandic emergency, icelandic alert, icelandic volcano, icelandic volcanoes, icelandic eruption, icelandic lava, icelandic smoke, icelandic ash, icelandic ashes, icelandic tectonic, icelandic tectonics, icelandic evacuation",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-labanpilipinas",

      "name": "LabanPilipinas",

      "totalCount": 83465,

      "language": "en",

      "curator": "rukku",

      "collectionCreationDate": 1409533397000,

      "endDate": 1409781610000,

      "startDate": 1409606740000,

      "keywords": "#LabanPilipinas",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 1,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-qatar_test",

      "name": "Qatar test",

      "totalCount": 36490,

      "language": "en",

      "curator": "bilalr",

      "collectionCreationDate": 1408954878000,

      "endDate": 1409694692000,

      "startDate": 1409602683000,

      "keywords": "#dohatraffic, #qatar, #doha",

      "geo": "51.399897,25.200085,51.648721,25.398936",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-pakistan_protests",

      "name": "Pakistan protests",

      "totalCount": 118195,

      "language": "en",

      "curator": "bilalr",

      "collectionCreationDate": 1409697283000,

      "endDate": 1409925602000,

      "startDate": 1409751831000,

      "keywords": "#Pakistan, Pakistan Army, Nawaz Sharif",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-pakistan_floods",

      "name": "Pakistan Floods 2014",

      "totalCount": 749250,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1410030794000,

      "endDate": 1411563601000,

      "startDate": 1411389836000,

      "keywords": "Flood, Floods, FloodsInPakistan,  ‪kashmirfloodrelief‬, KashmirFlood, PunjabFlood, LahoreFlood, JInabFlood, SialkotFlood, Landslide",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-gamergate",

      "name": "gamergate",

      "totalCount": 93160,

      "language": "en",

      "curator": "ajbangug",

      "collectionCreationDate": 1410427299000,

      "endDate": 1412838000000,

      "startDate": 1412664110000,

      "keywords": "#gamergate",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-starbucks",

      "name": "starbucks",

      "totalCount": 95,

      "language": "en",

      "curator": "ajbangug",

      "collectionCreationDate": 1410457393000,

      "endDate": 1410459281000,

      "startDate": 1410459227000,

      "keywords": "#starbucks",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "TwTracker",

      "name": "Twitter Tracker",

      "totalCount": 146675,

      "language": "ar",

      "curator": "SyriaTracker",

      "collectionCreationDate": 1410668751000,

      "endDate": 1410757200000,

      "startDate": 1410710614000,

      "keywords": "",

      "geo": "34.55,11.51,48.5,38.06",

      "status": "NOT_RUNNING",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-odile",

      "name": "Hurricane Odile in Mexico",

      "totalCount": 212110,

      "language": "en",

      "curator": "ChaToX",

      "collectionCreationDate": 1410767663000,

      "endDate": 1412085602000,

      "startDate": 1411912501000,

      "keywords": "hurricane odile, hurricaneodile, odile, hurricane cabo, hurricane baja california",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-ebola",

      "name": "ebola",

      "totalCount": 275575,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1408370257000,

      "endDate": 1411488000000,

      "startDate": 1411398092000,

      "keywords": "ebola",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "EBOLA",

      "name": "EBOLA 2104",

      "totalCount": 33415,

      "language": "en",

      "curator": "IamLGT",

      "collectionCreationDate": 1411457214000,

      "endDate": 1411548088000,

      "startDate": 1411457256000,

      "keywords": "#EBOLA,#2014, #AFRICA",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014_09-ebolaEbola",

      "name": "Ebola Virus",

      "totalCount": 18865,

      "language": "en",

      "curator": "iamJRrosales",

      "collectionCreationDate": 1411464267000,

      "endDate": 1411548089000,

      "startDate": 1411464442000,

      "keywords": "#ebola, #ebolavirus",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-hong_kong_protests",

      "name": "Hong Kong protests",

      "totalCount": 1715,

      "language": "en",

      "curator": "velofemme",

      "collectionCreationDate": 1411910136000,

      "endDate": 1411910631000,

      "startDate": 1411910153000,

      "keywords": "#occupycentral, hong kong, occupy central",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-protests_in_hong_kong_2014",

      "name": "Protests in Hong Kong 2014",

      "totalCount": 95223255,

      "language": "en",

      "curator": "velofemme",

      "collectionCreationDate": 1411911792000,

      "endDate": 1412085603000,

      "startDate": 1411911804000,

      "keywords": "#occupycentral, occupy central, hong kong, hong kong demonstration, pro-democracy",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 18,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-boko_haram",

      "name": "Boko Haram",

      "totalCount": 5,

      "language": "en",

      "curator": "SMCChief_West",

      "collectionCreationDate": 1412334747000,

      "endDate": 1412334911000,

      "startDate": 1412334802000,

      "keywords": "#bokoharam, boko haram, shekau",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-ebola2014",

      "name": "ebola2014",

      "totalCount": 921125,

      "language": null,

      "curator": "jeff_sole",

      "collectionCreationDate": 1407349560000,

      "endDate": 1412362800000,

      "startDate": 1407542871000,

      "keywords": null,

      "geo": null,

      "status": "STOPPED",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-tickbits",

      "name": "tickbits",

      "totalCount": 14305,

      "language": "en",

      "curator": "Jothirnadh",

      "collectionCreationDate": 1412156779000,

      "endDate": 1412633819000,

      "startDate": 1412633700000,

      "keywords": "#tekenbeet",

      "geo": "2.7,51.18,7.24,53.17",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-ebola_october_2014",

      "name": "Ebola October 2014",

      "totalCount": 6735,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1412690388000,

      "endDate": 1421667390000,

      "startDate": 1421663827000,

      "keywords": "#EbolaLR, #EbolaSL, #EbolaGN, #EbolaResponse, #EbolaNeed, Ebola, water",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-tropical_cyclone_hudhud",

      "name": "Tropical Cyclone HUDHUD",

      "totalCount": 393570,

      "language": "en",

      "curator": "SBTFJus",

      "collectionCreationDate": 1412782010000,

      "endDate": 1413907200000,

      "startDate": 1413298845000,

      "keywords": "Chittivalasa, Andhra Pradesh,Madhya Pradesh, Orissa, Maharashtra, #Indiacyclone, #Cyclone, HUDHUD, \nVishakhapatnam, Jagdalpur, Salur, Garbham, Vizianagaram, Bhogapuram\t, Anakapalle, Pawni,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-hudhud",

      "name": "HUDHUD",

      "totalCount": 16275,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1412784386000,

      "endDate": 1412960403000,

      "startDate": 1412784459000,

      "keywords": "hudhud, india cyclone",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-anti-_and_pro-semitism",

      "name": "Anti- and Pro-Semitism",

      "totalCount": 7614260,

      "language": "en",

      "curator": "ingmarweber",

      "collectionCreationDate": 1406197431000,

      "endDate": 1415198480000,

      "startDate": 1415197441000,

      "keywords": "uncivilised,\nkykes,\nknacker,\nknackers,\nshyster,\nshysters,\ngypo,\ngypos,\ncunt,\ncunts,\npeckerwood,\npeckerwoods,\nraghead,\nragheads,\ncripple,\ncripples,\nniggur,\nniggurs,\n\"yellow bone\",\n\"yellow bones\",\nmuzzie,\nmuzzies,\nniggar,\nniggars,\nnigger,\nniggers,\ngreaseball,\ngreaseballs,\n\"white trash\",\n\"white trashes\",\n\"nig nog\",\n\"nig nogs\",\nfaggot,\nfaggots,\n\"cotton picker\",\n\"cotton pickers\",\ndarkie,\ndarkies,\nhoser,\nhosers,\n\"Uncle Tom\",\n\"Uncle Toms\",\nJihadi,\nJihadis,\nretard,\nretards,\nhillbilly,\nhillbillies,\n\n\nfag,\nfags,\n\"trailer trash\",\n\"trailer trashes\",\npikey,\npikies,\nnicca,\nniccas,\ntranny,\ntrannies,\n\"porch monkey\",\n\"porch monkies\",\nbogan,\nbogans,\nwigger,\nwiggers,\nwetback,\nwetbacks,\nnigglet,\nnigglets,\nwigga,\nwiggas,\ndhimmi,\ndhimmis,\nhayseed,\ndhimmis,,\nhonkey,\nhonkies,\neurotrash,\neurotrashes,\nyardie,\nyardies,\n\"moon cricket\",\n\"moon crickets\",\n\"trailer park trash\",\n\"trailer park trashes\",\nniggah,\nniggahes,\nyokel,\nyokels,\nnigguh,\nnigguhes,\n\"camel jockey\",\n\"camel jockies\",\nhonkie,\nhonkies,\nniglet,\nniglets,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-vongfong",

      "name": "Vongfong",

      "totalCount": 0,

      "language": "en",

      "curator": "SBTFJus",

      "collectionCreationDate": 1413190654000,

      "endDate": null,

      "startDate": 1413190672000,

      "keywords": "VONGFONG-14, VONGFONG, Yamata, Okinawa, naha, Heianza\t, Kin Wan, Naha,Nishihara,Rota,Northern Mariana Islands,\nNakagusuku Wan,Oku, typhoon VONGFONG,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-ebola",

      "name": "Ebola-jothi",

      "totalCount": 8863765,

      "language": "en",

      "curator": "Jothirnadh",

      "collectionCreationDate": 1413474942000,

      "endDate": 1414418400000,

      "startDate": 1414158077000,

      "keywords": "#ebola",

      "geo": "-180.0,-90.0,180.0,90.0",

      "status": "TRASHED",

      "labelCount": 20,

      "humanTaggedCount": null

   },

   {

      "code": "wfd2014",

      "name": "World Food Day 2014",

      "totalCount": 6425,

      "language": "en",

      "curator": "ictlogist",

      "collectionCreationDate": 1413487656000,

      "endDate": 1413813605000,

      "startDate": 1413533225000,

      "keywords": "wfd2014",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-OTTAWA",

      "name": "Ottawa_22.10.2014_attacks",

      "totalCount": 652870,

      "language": "en",

      "curator": "BabigaBirregah",

      "collectionCreationDate": 1414013952000,

      "endDate": 1414620007000,

      "startDate": 1414013980000,

      "keywords": "Ottawa, #Ottawa, #ottawaStrong,  #canadaStrong,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-ebola1",

      "name": "Ebola Test",

      "totalCount": 2590,

      "language": "en",

      "curator": "ManzanaMecanica",

      "collectionCreationDate": 1414581333000,

      "endDate": 1414582113000,

      "startDate": 1414581409000,

      "keywords": "ebola, christie new jersey, ébola",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-remagen",

      "name": "Reinbek",

      "totalCount": 0,

      "language": "en,de",

      "curator": "Rebecca1708",

      "collectionCreationDate": 1414704907000,

      "endDate": 1414705164000,

      "startDate": 1414705019000,

      "keywords": "",

      "geo": "10.172665,53.4587,10.277839,53.543064",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "syria_af",

      "name": "This is an Afghan in Syria 123 your to wee the this tou you my you",

      "totalCount": 87315,

      "language": "en",

      "curator": "jikimlucas",

      "collectionCreationDate": 1414835658000,

      "endDate": 1415048400000,

      "startDate": 1414912223000,

      "keywords": "Syria, afghan, Iranians",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "2014-11-waga",

      "name": "wagah border blast",

      "totalCount": 2260,

      "language": "en",

      "curator": "johnypeterson",

      "collectionCreationDate": 1415003444000,

      "endDate": 1415108833000,

      "startDate": 1415003463000,

      "keywords": "#wagahborder, #blast, #atari #pakistan #bomb #suisidebomber #attack",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Violent_Geographies",

      "name": "Geographies of Violence",

      "totalCount": 137375,

      "language": "en",

      "curator": "ladyeloquence",

      "collectionCreationDate": 1415122435000,

      "endDate": 1415300371000,

      "startDate": 1415300397000,

      "keywords": "#sgbv, #gbv, #sexualviolence, #genderviolence, #gender-based violence, #rape, #discrimination, #lgbti, #hatespeech, #domesticviolence, #IPV, #VAW, #IPH, #intimatepartnerhomicide, #domestichomicide, #familyviolence",

      "geo": "-93.9,29.07,-89.09,33.06",

      "status": "NOT_RUNNING",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "2014-11-kobani_battle",

      "name": "kobani battle",

      "totalCount": 45880,

      "language": "en",

      "curator": "johnypeterson",

      "collectionCreationDate": 1415105509000,

      "endDate": 1415494801000,

      "startDate": 1415319162000,

      "keywords": "#Peshmerga, #Syria, #rebel, #battle, #IslamicState, #Kobani",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-11-bsd",

      "name": "BSD",

      "totalCount": 210,

      "language": "en",

      "curator": "UofCASE",

      "collectionCreationDate": 1415311747000,

      "endDate": 1415487617000,

      "startDate": 1415311765000,

      "keywords": "#bsd, #ucalgary, #uofc",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-11-typhoon_nuri",

      "name": "Typhoon Nuri",

      "totalCount": 5100,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1415346242000,

      "endDate": 1415530697000,

      "startDate": 1415530847000,

      "keywords": "typhoon nuri, #nuri, storm nuri, weather nuri, wind nuri, winds nuri, wave nuri, waves nuri, sea nuri, pacific nuri, satellite nuri, tropical nuri, damage nuri",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "128390298CEA-0000001",

      "name": "EMSC Earthquake-Triggered Geo",

      "totalCount": 722420,

      "language": "en,fr,es",

      "curator": "jikimlucas",

      "collectionCreationDate": 1410772825000,

      "endDate": 1446635969000,

      "startDate": 1446607816000,

      "keywords": "avalanche,landslide,seismic,earthquake,quake,avalanches,landslides,earthquakes,quakes,glissement,effondrement,éboulement,séisme,tremblement,avalanches,glissements,effondrements,éboulements,séismes,tremblements,avalancha,deslizamiento,derrumbe,sismo,temblor,terremoto,avalanchas,deslizamientos,derrumbes,sismos,temblores,terremotos",

      "geo": "97.36,-0.12,98.34,0.86",

      "status": "NOT_RUNNING",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "playing_it_my_way",

      "name": "Playing_it_my_way",

      "totalCount": 0,

      "language": "en",

      "curator": "johnypeterson",

      "collectionCreationDate": 1415561150000,

      "endDate": 1415673687000,

      "startDate": 1415674323000,

      "keywords": "#sachin, #sachin_tendulkar, #tendulkar, #cricket, #autobiography",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141117-0704-SophieChavanel-yolanda",

      "name": "Typhon Haiyan Yolanda",

      "totalCount": 43340,

      "language": "en",

      "curator": "SophieChavanel",

      "collectionCreationDate": 1416183041000,

      "endDate": 1416280831000,

      "startDate": 1416241815000,

      "keywords": "#Haiyan, #Yolanda, typhoon Philipines",

      "geo": "117.4,5.34,127.84,21.32",

      "status": "NOT_RUNNING",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "20141117-0901-SophieChavanel-earthquake_new_zealand",

      "name": "Earthquake New Zealand",

      "totalCount": 565,

      "language": "en",

      "curator": "SophieChavanel",

      "collectionCreationDate": 1416189812000,

      "endDate": 1416200284000,

      "startDate": 1416189833000,

      "keywords": "earthquake New Zealand, earthquake Bay of Plenty",

      "geo": "175.8068,-39.0079,178.1197,-36.8939",

      "status": "TRASHED",

      "labelCount": 25,

      "humanTaggedCount": null

   },

   {

      "code": "20141117-0119-SophieChavanel-earthquake",

      "name": "Earthquake New Zealand 2014",

      "totalCount": 20,

      "language": "en",

      "curator": "SophieChavanel",

      "collectionCreationDate": 1416205415000,

      "endDate": 1416214804000,

      "startDate": 1416213404000,

      "keywords": "earthquake new zealand",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20141117-0141-SophieChavanel-earthquake_new_zealand_(2014)",

      "name": "Earthquake New Zealand (2014)",

      "totalCount": 1225,

      "language": "en",

      "curator": "SophieChavanel",

      "collectionCreationDate": 1416206781000,

      "endDate": 1416241798000,

      "startDate": 1416241650000,

      "keywords": "earthquake New Zealand",

      "geo": "173.5462,-41.0675,178.7938,-36.1639",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20141118-1006-SophieChavanel-ebola",

      "name": "TEST Ebola 2014",

      "totalCount": 12005,

      "language": "en",

      "curator": "SophieChavanel",

      "collectionCreationDate": 1416286796000,

      "endDate": 1416333606000,

      "startDate": 1416286818000,

      "keywords": "#ebola",

      "geo": "-15.14,3.76,-7.27,12.74",

      "status": "NOT_RUNNING",

      "labelCount": 12,

      "humanTaggedCount": null

   },

   {

      "code": "20141120-1109-SinhaKoushik-new_york_snow_storm_2014",

      "name": "New York Snow Storm 2014",

      "totalCount": 16525,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1416471531000,

      "endDate": 1418635790000,

      "startDate": 1418635778000,

      "keywords": "#buffalosnowstorm, new york snow storm, new york snowstorm, #snowstorm",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "20141201-0517-Fidget02-dar_es_salam",

      "name": "Dar es Salaam baseline data",

      "totalCount": 8655,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1417454423000,

      "endDate": 1425658401000,

      "startDate": 1425658407000,

      "keywords": "Dar es Salaam flooding, dar es salaam flood",

      "geo": "39.186172,-6.882796,39.328872,-6.722768",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20141202-0234-RamianFathi-ferguson",

      "name": "Ferguson_TEst",

      "totalCount": 337175,

      "language": "en,",

      "curator": "RamianFathi",

      "collectionCreationDate": 1417527468000,

      "endDate": 1417701600000,

      "startDate": 1417527487000,

      "keywords": "#Ferguson, #police Ferguson",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "20141204-0242-julius_adebayo-test_crises",

      "name": "test_crises",

      "totalCount": 0,

      "language": "en",

      "curator": "julius_adebayo",

      "collectionCreationDate": 1417722150000,

      "endDate": 1417722333000,

      "startDate": 1417722171000,

      "keywords": "#flood #water #event",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141204-0244-julius_adebayo-test_another_crises",

      "name": "test_another_crises",

      "totalCount": 39115,

      "language": "en",

      "curator": "julius_adebayo",

      "collectionCreationDate": 1417722312000,

      "endDate": 1418557770000,

      "startDate": 1417722353000,

      "keywords": "#concert",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "20141204-1038-Stefan_Martini-typhonn_dez_2014",

      "name": "Typhonn Dez 2014",

      "totalCount": 0,

      "language": "",

      "curator": "Stefan_Martini",

      "collectionCreationDate": 1417729226000,

      "endDate": null,

      "startDate": 1417729246000,

      "keywords": "#RubyPH, #WalangPasok, #FloodPH, #RoadAlert, #NoPower, #NoWater, #StormSurgePH, #TsunamiPH, #RescuePH, #ReliefPH, #SafeNowPH, #MovePH",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "20141202-1140-PeterMosur-",

      "name": "Typhoon Hagupit",

      "totalCount": 625890,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1417581881000,

      "endDate": 1418729167000,

      "startDate": 1418557548000,

      "keywords": "hagupit, typhoon philippines, #typhoonhagupit, #supertyphoonhagupit, #RubyPH, #RescuePH, #ReliefPH, #FloodPH, #MovePH, #StormSurgePH, #HagupitPH, #TracingPH, #WalangPasok, #WalayKlase, #PrayForThePhilippines, philippines #RoadAlert, philippines #NoPower, philippines #NoWater, philippines #commisaid, philippines #SafeNow, philippines help, philippines aid, philippines rescue, philippines shelter, philippines humanitarian, philippines urgent",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 6,

      "humanTaggedCount": null

   },

   {

      "code": "20141208-1211-reddysiva-ebola_treatment",

      "name": "Ebola Treatment",

      "totalCount": 1069455,

      "language": "en",

      "curator": "reddysiva",

      "collectionCreationDate": 1418015702000,

      "endDate": 1420967331000,

      "startDate": 1420967323000,

      "keywords": "Ebola, #ebola,  Brincidofovir, ebola tratment, ZMapp, TKM-Ebola, ebola vaccine",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "20141208-0514-Cluency-stampede",

      "name": "Ferguson 2014",

      "totalCount": 125,

      "language": "en",

      "curator": "Cluency",

      "collectionCreationDate": 1418048146000,

      "endDate": 1418048776000,

      "startDate": 1418048545000,

      "keywords": "#stampede, #riot, #police, #military",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141215-0303-reyreina-banjir",

      "name": "Banjir Jakarta",

      "totalCount": 0,

      "language": "id",

      "curator": "reyreina",

      "collectionCreationDate": 1418623457000,

      "endDate": null,

      "startDate": 1420516908000,

      "keywords": "banjir jakarta",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141215-0238-HeatherLeson-qatar_national_day",

      "name": "Qatar National Day",

      "totalCount": 286655,

      "language": "en",

      "curator": "HeatherLeson",

      "collectionCreationDate": 1418672426000,

      "endDate": 1418750507000,

      "startDate": 1418750508000,

      "keywords": "QND2014, qatar national day, qnd2014, ndqatar, qatar natl day, qatar nat day, qatar dec 18, qatar december 18, qatar celebrates day",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "20141216-0112-ChaToX-pashawar_attack",

      "name": "Peshawar Attack",

      "totalCount": 1793450,

      "language": "en",

      "curator": "ChaToX",

      "collectionCreationDate": 1418724823000,

      "endDate": 1419759569000,

      "startDate": 1418724843000,

      "keywords": "PeshawarAttack, Peshawar Attack, attack school taliban, attack school pakistan, school dead pakistan, school killed pakistan, school injured pakistan, school peshawar, children dead pakistan, children shot pakistan",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20141216-1055-mimran15-",

      "name": "Qatar National Day (Arabic)",

      "totalCount": 786225,

      "language": "ar",

      "curator": "mimran15",

      "collectionCreationDate": 1418716710000,

      "endDate": 1418722197000,

      "startDate": 1418751026000,

      "keywords": "QND2014, qatar national day, qnd2014, ndqatar, qatar natl day, qatar nat day, qatar dec 18, qatar december 18, qatar celebrates day",

      "geo": "50.6829,24.4508,52.4348,26.255",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141214-0441-amelie_sundberg-ira",

      "name": "Falluja Daesh",

      "totalCount": 4310,

      "language": "en",

      "curator": "amelie_sundberg",

      "collectionCreationDate": 1418564527000,

      "endDate": 1419769715000,

      "startDate": 1419769730000,

      "keywords": "fallujah",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141229-1136-BorgeHolthoefer-political",

      "name": "political",

      "totalCount": 255,

      "language": "en",

      "curator": "BorgeHolthoefer",

      "collectionCreationDate": 1419842265000,

      "endDate": 1419842912000,

      "startDate": 1419842913000,

      "keywords": "mobilization, \"political meeting\", political demonstration, massive demonstration, protest, political sit-in, demonstrators street",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20141228-0228-sinha_kou-airasia",

      "name": "AirAsia8501",

      "totalCount": 0,

      "language": "en",

      "curator": "sinha_kou",

      "collectionCreationDate": 1419766228000,

      "endDate": null,

      "startDate": 1419769674000,

      "keywords": "AirAsia, air asia, QZ8501, Z8501, 8501, 162 passengers, AirAsiaMissing, air asia missing, AirAsia missing",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141230-0403-HeatherLeson-tropical_storm_jangmi",

      "name": "Tropical Storm Jangmi",

      "totalCount": 1600,

      "language": "en",

      "curator": "HeatherLeson",

      "collectionCreationDate": 1419973445000,

      "endDate": 1421830804000,

      "startDate": 1421657568000,

      "keywords": "Tropical Storm Jangmi, Mindanao Island",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 17,

      "humanTaggedCount": null

   },

   {

      "code": "20150122-0409-mimran15-tweets_in_urdu_on_misc_topics",

      "name": "Tweets in Urdu on Misc Topics",

      "totalCount": 16745,

      "language": "ur",

      "curator": "mimran15",

      "collectionCreationDate": 1421932309000,

      "endDate": 1422518401000,

      "startDate": 1422344114000,

      "keywords": "پاکستان,  پٹرول",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "20150126-0155-SinhaKoushik-test_koushik",

      "name": "Test Koushik Collection",

      "totalCount": 28120,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1422269764000,

      "endDate": 1435058914000,

      "startDate": 1435053907000,

      "keywords": "obama, republic day, modi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "20150127-0316-SoylentBleen-snowmageddon2015",

      "name": "snowmageddon2015",

      "totalCount": 0,

      "language": "en",

      "curator": "SoylentBleen",

      "collectionCreationDate": 1422400608000,

      "endDate": null,

      "startDate": 1422400627000,

      "keywords": "blizzard",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 24,

      "humanTaggedCount": null

   },

   {

      "code": "20150128-1057-liliannqatar-geo_tester",

      "name": "geo tester",

      "totalCount": 26535,

      "language": "en",

      "curator": "liliannqatar",

      "collectionCreationDate": 1422475091000,

      "endDate": 1423141207000,

      "startDate": 1422534973000,

      "keywords": "",

      "geo": "50.7117,24.4702,51.7419,26.1754",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20150209-0756-robinusantara1-banjir",

      "name": "banjir",

      "totalCount": 0,

      "language": "id",

      "curator": "robinusantara1",

      "collectionCreationDate": 1423443459000,

      "endDate": 1423482077000,

      "startDate": 1423443828000,

      "keywords": "#Prayforkalbar",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150209-0638-robinusantara1-banjir_jakarta",

      "name": "Banjir Wilayah Jakarta",

      "totalCount": 1595,

      "language": "en,id",

      "curator": "robinusantara1",

      "collectionCreationDate": 1423482073000,

      "endDate": 1423636443000,

      "startDate": 1423636464000,

      "keywords": "#JakartaBanjir",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 35,

      "humanTaggedCount": null

   },

   {

      "code": "20150211-1108-rharihprasad-example_crisis",

      "name": "Example crisis",

      "totalCount": 35,

      "language": "en",

      "curator": "rharihprasad",

      "collectionCreationDate": 1423681765000,

      "endDate": 1423682214000,

      "startDate": 1423681789000,

      "keywords": "blizzard",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "20150217-0623-skohail-gaza_war",

      "name": "Gaza war",

      "totalCount": 0,

      "language": "en",

      "curator": "skohail",

      "collectionCreationDate": 1424193839000,

      "endDate": 1424194023000,

      "startDate": 1424193859000,

      "keywords": "#gaza #strike #war",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150209-1205-PeterMosur-qatar_airways",

      "name": "Qatar Airways",

      "totalCount": 355,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1423501552000,

      "endDate": 1424716348000,

      "startDate": 1424588310000,

      "keywords": "qatar airways, qatar airway",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150219-0352-Fidget02-american_red_cross",

      "name": "American Red Cross",

      "totalCount": 0,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1424361163000,

      "endDate": 1424708883000,

      "startDate": 1424691559000,

      "keywords": "#GiveWhat FireTakes, Alabama\nAlabama  Earthquake,  Alabama     Flood, Alabama   Home Fire, Alabama Hurricane, Alabama  Mass Casualty, Alabama   Mudslide, Alabama Landslide,  Alabama Avalanche, Alabama Pandemic, Alabama  Tornado, Alabama  Volcano, Alabama   Wildfire, Alabama Winter Storm, Arizona Earthquake,  Arizona     Flood, Arizona   Home Fire, Arizona   Hurricane, Arizona  Mass Casualty, Arizona  Mudslide, Arizona Landslide,  Arizona Avalanche, Arizona Pandemic, Arizona Tornado, Arizona Volcano, Arizona Wildfire, Arizona   Winter Storm, Arkansas Earthquake,  Arkansas    Flood, Arkansas  Home Fire, Arkansas  Hurricane, Arkansas  Mass Casualty, Arkansas  Mudslide, Arkansas Landslide,  Arkansas Avalanche, Arkansas Pandemic, ArkansasTornado, Arkansas Volcano, Arkansas Wildfire, Arkansas  Winter Storm, California Earthquake,  California   Flood, California Home Fire, California  Hurricane, California  Mass Casualty, California  Mudslide, California Landslide,  California Avalanche, Cali",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150221-1149-haewoon-#motherlanguage",

      "name": "#MotherLanguage",

      "totalCount": 530,

      "language": "",

      "curator": "haewoon",

      "collectionCreationDate": 1424551787000,

      "endDate": 1424605297000,

      "startDate": 1424551812000,

      "keywords": "#MotherLanguage",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150222-0654-JoranLindeberg-guatemala_human_rights_information",

      "name": "Guatemala Human Rights information",

      "totalCount": 225,

      "language": "es",

      "curator": "JoranLindeberg",

      "collectionCreationDate": 1424652939000,

      "endDate": 1423602000000,

      "startDate": 1425528522000,

      "keywords": "ddhh pnc, pdh, pbi, mp, copredeh, ejercito guatemala, , ejercito guate, pgn guatemala, pgn guate, oacnudh, derechos guatemala, derechos guate, onu guatemala, onu guate, riosmontt, rios montt, genocidio, udefegua, enfrentamientos pnc,  bomba guatemala, bomba guate, bomba pnc, violacion pnc, acoguate, caldh, ajr, famdegua, fuerza de tarea, san juan sacatepequez, embajada suecia guatemala, embajada suecia guate, embajada noruega guatemala, embajada noruega guate conflictividad guate, conflictividad guatemala, conflictividad pnc, arco teculutan, nacahuil, tajumulco, mina marlin,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "20150115-0141-ephan17-",

      "name": "HIV Treatment Questions",

      "totalCount": 322931,

      "language": null,

      "curator": "ephan17",

      "collectionCreationDate": 1421322103000,

      "endDate": 1445993562000,

      "startDate": 1445943641000,

      "keywords": null,

      "geo": null,

      "status": "NOT_RUNNING",

      "labelCount": 9,

      "humanTaggedCount": null

   },

   {

      "code": "20150225-0225-JusMack13-american_red_cross_v3",

      "name": "American Red Cross v3",

      "totalCount": 2360,

      "language": "en",

      "curator": "JusMack13",

      "collectionCreationDate": 1424874577000,

      "endDate": 1426606080000,

      "startDate": 1425990615000,

      "keywords": "#GiveWhat FireTakes, Alabama, Alabama  Earthquake,  Alabama Flood, Alabama   Home Fire, Alabama Hurricane, Alabama  Mass Casualty, Alabama   Mudslide, Alabama Landslide,  Alabama Avalanche, Alabama Pandemic, Alabama  Tornado, Alabama  Volcano, Alabama   Wildfire, Alabama Winter Storm, Arizona Earthquake,  Arizona     Flood, Arizona   Home Fire, Arizona   Hurricane, Arizona  Mass Casualty, Arizona  Mudslide, Arizona Landslide,  Arizona Avalanche, Arizona Pandemic, Arizona Tornado, Arizona Volcano, Arizona Wildfire, Arizona   Winter Storm Arkansas Earthquake,  Arkansas  Flood, Arkansas  Home Fire, Arkansas  Hurricane, Arkansas  Mass Casualty, Arkansas  Mudslide, Arkansas Landslide,  Arkansas Avalanche, Arkansas Pandemic, ArkansasTornado, Arkansas Volcano, Arkansas Wildfire, Arkansas  Winter Storm, California Earthquake,  California   Flood, California Home Fire, California  Hurricane, California  Mass Casualty, California  Mudslide, California Landslide,  California Avalanche,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 9,

      "humanTaggedCount": null

   },

   {

      "code": "20150302-1149-tinacomes-ebola",

      "name": "Ebola Africa",

      "totalCount": 10010,

      "language": "en",

      "curator": "tinacomes",

      "collectionCreationDate": 1425293542000,

      "endDate": 1425928879000,

      "startDate": 1425928886000,

      "keywords": "Ebola Response",

      "geo": "-18.14,4.09,1.23,18.08",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150211-0351-jlucasQatar-crisis_boundingbox_test",

      "name": "EMSC KEYWORD TEST",

      "totalCount": 1095,

      "language": "en,fr,it,es",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1423659295000,

      "endDate": 1425895827000,

      "startDate": 1425895510000,

      "keywords": "FOOD",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150309131049_emsc_local_test_by_keyword",

      "name": "emsc local test by keyword",

      "totalCount": 3975,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1425895903000,

      "endDate": 1426604583000,

      "startDate": 1426603601000,

      "keywords": "rockslide, snowslide, snowslip, mudslide -cream -icecream, \"rock fall\", avalanche affected, avalanche alert, avalanche buried, avalanche buries, avalanche bury, avalanche casualties, avalanche collapse, avalanche collapses, avalanche dead, avalanche debris, avalanche die, avalanche disaster, avalanche earthquake, avalanche evacuated, avalanche fatal, avalanche flood, avalanche floods, avalanche ground, avalanche hill, avalanche hills, avalanche home, avalanche homes, avalanche houses, avalanche ice, avalanche injured, avalanche kill, avalanche kills, avalanche lake, avalanche missing, avalanche mountain, avalanche mud, avalanche rain, avalanche repair, avalanche repairs, avalanche rescue, avalanche risk, avalanche river, avalanche road, avalanche roadblock, avalanche rock, avalanche rocks, avalanche search, avalanche slab, avalanche slope, avalanche snow, avalanche snowfall, avalanche snowpack, avalanche storm, avalanche storms, avalanche survives, avalanche survivor",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150311152434_cyclone_pam-15",

      "name": "Cyclone PAM-15",

      "totalCount": 494480,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1426087966000,

      "endDate": 1427626804000,

      "startDate": 1427366864000,

      "keywords": "cyclone, Cyclone PAM, Mohoun'gha, Vanuatu, Aniwa, Dillons Bay, Futuna Island, Ipota, Lamap, Quine Hill, Forari Bay, cyclone Vanuatu, Hunter Island, New Caledonia, Matthew Island, Aneityum Island, Tana Island, flooding Vanuatu, #TCPam #CyclonePam, Cyclone, CyclonePam, Pam, TCPam, Vanuatu, #Vanuatu, #ERROMANGO, #TANNA, #ANEITYUM, #ANIVA, #FUTUNA, #VANUATU, SUPER CYCLONE #PAM(295kph), #EMAO, #PELE, #NGUNA, #MOSO, #LELEPA, #KAKULA, #ERETOKA",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "150312104037_emsc_landslides_by_kw_fra",

      "name": "EMSC Landslides by KW fra",

      "totalCount": 26485,

      "language": "fr",

      "curator": "ChaToX",

      "collectionCreationDate": 1426146072000,

      "endDate": 1445920589000,

      "startDate": 1445921090000,

      "keywords": "avalanche accident, avalanche affecte, avalanche aide, avalanche alerte, avalanche assistance, avalanche blessées, avalanche bloque route, avalanche boue, avalanche chemin, avalanche colline, avalanche collines, avalanche débris, avalanche déclenchement, avalanche désastre, avalanche détruites, avalanche disparu, avalanche effondrement, avalanche effondrements, avalanche endommagé, avalanche endommagées, avalanche enseveli, avalanche ensevelie, avalanche ensevelis, avalanche évacuer, avalanche glace, avalanche glissement, avalanche inondation, avalanche inondations avalanche lac, avalanche maison, avalanche maisons, avalanche marquante, avalanche meurent, avalanche montagne, avalanche mort, avalanche mortelle, avalanche morts, avalanche neige, avalanche pente, avalanche pierre, avalanche pierres, avalanche piste, avalanche pistes, avalanche pluie, avalanche recherche, avalanche réparation, avalanche réparer, avalanche risque avalanche risques, avalanche riviére avalanche roches, avalanche route, avalanche sauve, avalanche sauvé, avalanche sauvetage, avalanche séisme, avalanche sol, avalanche pierres, avalanche survie, avalanche survivants avalanche tempête, avalanche tempêtes, avalanche tué, avalanche tuées, avalanche victimes, chiens d'avalanche, danger d'avalanche, risque d'avalanche, chutes de neige avalanche, chute blessé, chute blessés, chute glacier, chute lave, chute meurent, chute mort, chute morts, chute pierres, chute séisme, chute sol, chute terrain, chute terrains, chute terre, chute tremblement terre, éboulement, éboulement meurtrier, éboulement tranche, éboulement tranchée, effondrement boue, effondrement neige, effondrement pierres, effondrement roche, effondrement roches, glissement blessé, glissement blessés, glissement boue, glissement glacier, glissement lave, glissement meurent, glissement meurtrier, glissement montagne glissement montagnes, glissement mort, glissement morts, glissement pierres, glissement séisme, glissement sol, glissement terrain, glissement terrains, glissement terre, glissement tremblement",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "150312104424_",

      "name": "EMSC Landslides by KW esp",

      "totalCount": 41035,

      "language": "es",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1426146302000,

      "endDate": 1445844568000,

      "startDate": 1446293089000,

      "keywords": "avalancha afectados, avalancha alerta, avalancha barro, avalancha basura, avalancha bloqueo camino, avalancha búsqueda, avalancha camino, avalancha casas, avalancha cerro, avalancha cerros, avalancha colapso, avalancha colapsos, avalancha desastre, avalancha enterrados, avalancha enterró, avalancha entierra, avalancha evacuados, avalancha fatal, avalancha glacial, avalancha glaciar, avalancha heridos, avalancha hielo, avalancha hielos, avalancha hogar, avalancha hogares, avalancha inundación, avalancha inundaciones, avalancha lago, avalancha lava, avalancha lluvias, avalancha lodo, avalancha montaña, avalancha muere, avalancha mueren, avalancha muerto, avalancha muertos, avalancha nieve, avalancha pendiente, avalancha perdidos, avalancha piedras, avalancha reparaciones, avalancha reparar, avalancha rescate, avalancha riesgo, avalancha río, avalancha roca, avalancha rocas, avalancha sismo, avalancha sobreviven, avalancha suelo, avalancha temblor, avalancha terremoto, avalancha terreno, avalancha terrenos, avalancha tierra, avalancha tormentas, caída nieve, caída rocas, derrumbe barro, derrumbe cerro, derrumbe cerros, derrumbe lodo, derrumbe montaña, derrumbe montañas, derrumbe nieve, derrumbe piedras, derrumbe roca, derrumbe rocas, derrumbe terreno, derrumbe terrenos, deslizamiento heridos, deslizamiento lodo, deslizamiento mueren, deslizamiento muerto, deslizamiento muertos, deslizamiento nieve, deslizamiento piedras, deslizamiento sismo, deslizamiento suelo, deslizamiento temblor, deslizamiento terreno, deslizamiento terrenos, deslizamiento tierra, derrumbe temblor",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "150316151518_tdf_2015",

      "name": "TDF 2015",

      "totalCount": 75,

      "language": "en,it",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1426515389000,

      "endDate": 1426516302000,

      "startDate": 1426515549000,

      "keywords": "#Terra dei fuochi, #roghicampania, #rifiuticampania, #immondiziacampania",

      "geo": "13.846841,40.716838,14.846756,41.189503",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150316141936_pam",

      "name": "pam",

      "totalCount": 75,

      "language": "en",

      "curator": "Fra1871",

      "collectionCreationDate": 1426512059000,

      "endDate": 1426606020000,

      "startDate": 1426602960000,

      "keywords": "#Pam",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150316151352_pam",

      "name": "PAM-2015",

      "totalCount": 470,

      "language": "en",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1426515240000,

      "endDate": 1426939212000,

      "startDate": 1426762983000,

      "keywords": "#pamcyclone, #pam",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150318124843_riot_frankfurz_am_main",

      "name": "Riot Frankfurt am Main",

      "totalCount": 20830,

      "language": "en,de",

      "curator": "Stefan_Martini",

      "collectionCreationDate": 1426679576000,

      "endDate": 1426833308000,

      "startDate": 1426763584000,

      "keywords": "#18null3, #18nulldrei, #ECB, #Blockupy, #EZB, #m18, #Frankfurt",

      "geo": "8.369658,49.955847,8.962943,50.300595",

      "status": "NOT_RUNNING",

      "labelCount": 28,

      "humanTaggedCount": null

   },

   {

      "code": "150319192857_",

      "name": "Syria8647326",

      "totalCount": 95,

      "language": "en",

      "curator": "tester77073702",

      "collectionCreationDate": 1426786320000,

      "endDate": 1427533200000,

      "startDate": 1427359238000,

      "keywords": "syria remittance,syria bank,syria western union, syria money transfer,syria send money,syria moneygram",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "150320180554_guyana_elections_2015",

      "name": "Test Data - Guyana Pilot",

      "totalCount": 5390,

      "language": "en",

      "curator": "lutherjeke",

      "collectionCreationDate": 1426889191000,

      "endDate": 1428699600000,

      "startDate": 1430891444000,

      "keywords": "Guyana Rally, Guyana Voter registration, Guyana Voters’ list, Guyana Voter card Guyana Buying votes, Guyana Campaign vandalism, Guyanase not permitted to vote, Guyana Polling station closed, Guyana Ballot box, Guyana Ballot, Guyana Ballot counting, Guyana, Guyana Political party agents, Guyana Election observers, Guyana Ballot counting, Guyana Vote fraud, Guyana Polling station, Guyana Police, Guyana Court, Guyana Electoral Cycle, Guyana Elections Commission, GECOM, Guyana Elections Commission Media Monitoring Unit, Guyana Ethnic Relations Commission, Guyana ERC, Guyana Political Parties, People’s Progressive Party/Civic, PPP/C, A Partnership for National Unity, APNU, Alliance for Change, Guyana AFC, People’s National Congress, Guyana PNC, The United Force, TUF, Guyana Action Party/Rise Organise and Rebuild Guyana, GAP/ROAR, Guyana Democratic Party, Guyana Justice for All Party, National Front Alliance, Peoples Democratic Party Guyana, Guyana Working Peoples Alliance, Guyana Trades Union Congress, GTUC, Kaieteur News, Dr. Steve Surujbally, Dr. Roger Luncheon, Gail Teixeira, Dr. Rupert Roopnaraine, Dr. Leslie Ramsammy, Dr. Clive Jagan, Guyana Elections Region 3 – West Demerara-Essequibo Islands, Guyana North Ruimveldt Park Guyana Elections, Guyana Elections Kitty Market Square, Guyana Elections Hope West, Guyana Elections Enmore, Guyana Elections, Guyana Elections East Coast Demerara, Guyana Elections Miachony, Guyana Elections Berbice",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 9,

      "humanTaggedCount": null

   },

   {

      "code": "150326144742_earthquake",

      "name": "earthquake  in china 2",

      "totalCount": 10,

      "language": "en",

      "curator": "nihaoalison",

      "collectionCreationDate": 1427395745000,

      "endDate": 1427569202000,

      "startDate": 1427395902000,

      "keywords": "earthquake in china",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150325144959_hurricane",

      "name": "earhtquake",

      "totalCount": 45,

      "language": "en",

      "curator": "katyprogrammer",

      "collectionCreationDate": 1427266238000,

      "endDate": 1427419841000,

      "startDate": 1427266287000,

      "keywords": "#earthquake #california",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 2,

      "humanTaggedCount": null

   },

   {

      "code": "150312125437_nigeria_elections",

      "name": "Nigeria Elections",

      "totalCount": 4106145,

      "language": "en",

      "curator": "Spaceman1111111",

      "collectionCreationDate": 1426179279000,

      "endDate": 1428452270000,

      "startDate": 1428342857000,

      "keywords": "Nigeria elections, nigeriadecides, Nigeria decides, INEC, GEJ, Change Nigeria, Nigeria Transformation, President Jonathan, Goodluck Jonathan, Sai Buhari, saibuhari, All progressives congress, Osibanjo, Sambo, Peoples Democratic Party, boko haram, boko, area boys, nigeria2015, votenotfight, GEJwinsit, iwillvoteapc, gmb2015, thingsmustchange, revoda, march4buhari",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 6,

      "humanTaggedCount": null

   },

   {

      "code": "150330112457_earthquake_png",

      "name": "Earthquake PNG",

      "totalCount": 25,

      "language": "en",

      "curator": "hag_kim",

      "collectionCreationDate": 1427711097000,

      "endDate": 1427886016000,

      "startDate": 1427711520000,

      "keywords": "papua new guinea PNG earthquake kokopo",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150409100744_test-collection-t",

      "name": "test-Collection-t",

      "totalCount": 6816670,

      "language": "en",

      "curator": "test_socialq",

      "collectionCreationDate": 1428563304000,

      "endDate": 1429183147000,

      "startDate": 1429182601000,

      "keywords": "fashion, London, uk",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150415110524_yarmouk_refugee_camp",

      "name": "Yarmouk Refugee Camp",

      "totalCount": 4430,

      "language": "en",

      "curator": "timolue",

      "collectionCreationDate": 1429088796000,

      "endDate": 1429369200000,

      "startDate": 1429195605000,

      "keywords": "Yarmouk, Yarmuk, #Yarmouk, #Yarmuk",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150409113342_emsc_landslides_by_kw_ind",

      "name": "EMSC Landslides by KW ind",

      "totalCount": 0,

      "language": "en,id",

      "curator": "emscls10",

      "collectionCreationDate": 1428572074000,

      "endDate": 1445920600000,

      "startDate": 1446274474000,

      "keywords": "tanah longsor",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150416155204_yemen_overview",

      "name": "June Task Overview - Yemen",

      "totalCount": 8260820,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1429195988000,

      "endDate": 1445920601000,

      "startDate": 1446121342000,

      "keywords": "Sabaean Kingdom, Queen of Saba, Mudbrick  houses, Al Hodeidah, Kawkaban, Dar al Hajar, Tawilah Tanks Crater, Ibb, Song of Sana'a, Al-Ghina al-San’ani, Al-Ghuna al-San’ani, Magyal,Hadi Mosque, conflict, war, markets, food commodities, non-food commodities, bread, food crop, food transportation, fuel, diesel, water, food shortage, shortage, market closure, food supply, supply chain, food traders, bread, rice, wheat, meat, fish, eggs, cereals, staples, food harvest, limited food, displaced, infant milk, food assistance, food aid, World food programme, electricity, cooking gas, food unavailability, food access, famine, blockade, food insecurity, borrow food, food consumption, food needs, market closure, food monopoly, agriculture,",

      "geo": "41.82,12.11,54.53,19.0",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "150419170530_aidr_demo",

      "name": "aidr demo - test only",

      "totalCount": 243580,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1429452579000,

      "endDate": 1429464657000,

      "startDate": 1429453087000,

      "keywords": "children, starvation, house, school, water",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150419204912_aidrtest_demo",

      "name": "aidrTest Demo",

      "totalCount": 3130,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1429465850000,

      "endDate": 1429876811000,

      "startDate": 1429702762000,

      "keywords": "",

      "geo": "51.131762,25.089758,51.740044,25.760011",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150420115435_yemen_2",

      "name": "Yemen Conflict",

      "totalCount": 135,

      "language": "en",

      "curator": "JusMack13",

      "collectionCreationDate": 1429527851000,

      "endDate": 1430758800000,

      "startDate": 1430150827000,

      "keywords": "",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150422083426_doha_local_demo",

      "name": "Doha_Local_Demo",

      "totalCount": 0,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1429680948000,

      "endDate": 1429693155000,

      "startDate": 1429690822000,

      "keywords": "#navi, navi, test, demo, navi",

      "geo": "51.179379,25.150024,51.684902,25.496233",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150425104337_nepal_earthquake",

      "name": "Nepal Earthquake",

      "totalCount": 3004925,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1429955573000,

      "endDate": 1432006549000,

      "startDate": 1431854448000,

      "keywords": "Basantapur, Patan, Anamnagar,Bhaktapur,Durbar Square, Nuwakot, Dharahara Tower, Gorkha, Lamjung, Khudi, Kathmandu, Sankhu, Sunsari,Solu district, Okhaldhunga, Nepal, nepal earthquake, ktmearthquake, IndiaWithNepal, NepalQuake, NepalQuakeRelief, NepalEarthquake, KathmanduQuake, KathmanduQuakeRelief, KathmanduEarthauqke, QuakeNepal, EarthquakeNepal, QuakeKathmandu, EarthquakeKathmandu, PrayForNepal",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 20,

      "humanTaggedCount": null

   },

   {

      "code": "150504140443_katetestdemo",

      "name": "KateTestDemo",

      "totalCount": 2341080,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1430737525000,

      "endDate": 1430969971000,

      "startDate": 1430737546000,

      "keywords": "water, crisis, nepal",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150504144839_nepalearthquake",

      "name": "NepalEarthquake",

      "totalCount": 0,

      "language": "en",

      "curator": "JulienLouton",

      "collectionCreationDate": 1430743756000,

      "endDate": 1430814095000,

      "startDate": 1430743783000,

      "keywords": "Nepal\nEarthquakeNepal\nNepalEarthquake",

      "geo": "-159.6,70.0,-179.4,-62.9",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150506010009_guyana_2015_elections",

      "name": "Guyana Elections",

      "totalCount": 0,

      "language": "en",

      "curator": "lutherjeke",

      "collectionCreationDate": 1430888516000,

      "endDate": 1431459706000,

      "startDate": 1435345412000,

      "keywords": "#Votelikeaboss, Guyana ballot, Guyana politics, guyanaelection, guyanavote, Guyana election, Guyana Elections 2015, Guyana Elections2015, Guyana opposition, Guyana vote 2015, Guyana vote2015, Guyana Youth, Guyana rally, Guyana voter registration, Guyana voter, Guyana vote, Guyana votes, Guyana buying votes, Guyana campaign, Guyana election violence, Guyana polling station, Guyana poll, Guyana ballot box, Guyana election observers, Guyana observers, GECOM, Guyana Media Monitoring Unit, Guyana PPP, Guyana PPPC, PPPC, PPP/C, APNU, APNUAFC, Guyana APNU, Guyana AFC, PPP/Civic, GTUC, Roopnaraine, Guyana elections, Guyana PNC, APNU+AFC, Guyana TUF, Guyana party, Guyana election police, @APNUGuyana, Guyana elections commission, @stabroeknews, Kaieteur News, Guyana MMU, @UnitedGuyana, UnitedGuyana, Guyana PPP, APNUGuyana, NCN Guyana, Guyana National Youth Council, NCNGuyana, Guyana campaign vandalism, Guyana Voter's List, Guyana fraud, Guyana vandalism, GAP/ROAR, Guyana democracy, Guyana democratic, Guyana justice, Guyana Working People Alliance, Leslie Ramsammy, Clive Jagan, Guyana Jagan, Guyana Jagdeo, Guyana Harper, Guyana May11, Guyana May 11, Guyana intimidation, Guyana gunshots, Guyana protest, Guyana unrest, Guyana assault, Guyana attack, Guyana racial, Guyana race, Guyana ethnicity, Guyana burn, Guyana burning, Guyana arson, Guyana fire, Guyana security, Guyana beat up, Guyana threats, Guyana misconduct, Guyana corruption, Guyana corrupt, Guyana posters, Guyana death, Guyana injured, Guyana, #Guyana, Paramakatoi, Demerara, Mahaica, Berbice, Corentyne, Anna Regina, Pomeroon, Supenaam, Bartica, Cuyuni, Mazaruni, Skeldon, Rosignol, Potaro, Siparuni, Parika, Essequibo, Aishalton, Apoteri, Arakaka, Arimu Mine, Baramita, Bush Lot, Biloku, Berbice, Stewartvliie, Clonbrook, Cove & John, Hosororo, Imbaimadai",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 5,

      "humanTaggedCount": null

   },

   {

      "code": "150514165025_humanitarian_nepal",

      "name": "Humanitarian Nepal",

      "totalCount": 12595,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1431618996000,

      "endDate": 1432725282000,

      "startDate": 1432232365000,

      "keywords": "nepal rape, nepal gang-rape, nepal protest,nepal intimidation,nepal discrimination, nepal traffiker, nepal traffikers,nepal  denied, nepal healthcare,nepal water, nepal food, nepal demonstration, nepal hate speech,nepal  protest, nepal riot, nepal exploitation, nepal trafficker, nepal traffickers,nepal  human rights, nepal killing,nepal murder, nepal looting,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150517124204_twbdemo",

      "name": "TWBDemo-new server",

      "totalCount": 917220,

      "language": "es,fr,de,ja,zh,ko",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1431855776000,

      "endDate": 1437068716000,

      "startDate": 1437046340000,

      "keywords": "nepal, earthquake",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "150524154840_nepal_earthquake",

      "name": "Nepal disaster_nasia",

      "totalCount": 63670,

      "language": "en,el",

      "curator": "nasiou_la",

      "collectionCreationDate": 1432475383000,

      "endDate": 1432571098000,

      "startDate": 1432475615000,

      "keywords": "Νεπαλ, Nepal",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "150526071549_avalancha_salgar_antioquia_colombia",

      "name": "Avalancha Salgar Antioquia Colombia",

      "totalCount": 13650,

      "language": "es",

      "curator": "kublaykan",

      "collectionCreationDate": 1432642705000,

      "endDate": 1433259894000,

      "startDate": 1432994165000,

      "keywords": "Salgar, avalancha, #Salgar, #FuerzaSalgar, #JuntosPorSalgar,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 22,

      "humanTaggedCount": null

   },

   {

      "code": "150528114107_fifa",

      "name": "FIFA",

      "totalCount": 36280,

      "language": "en",

      "curator": "timolue",

      "collectionCreationDate": 1432806236000,

      "endDate": 1432821940000,

      "startDate": 1432806257000,

      "keywords": "fifa corruption, fifa arrests, fifa bribery, blatter corruption, fifa corrupt, fifa bribe, blatter corrupt, fifa bribes,",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150601114324_meghna_test",

      "name": "meghna_test",

      "totalCount": 190,

      "language": "en",

      "curator": "muskanalways",

      "collectionCreationDate": 1433148307000,

      "endDate": 1433148477000,

      "startDate": 1434613135000,

      "keywords": "#Kargil,India",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150604151515_",

      "name": "Yemen humanitarian crisis",

      "totalCount": 0,

      "language": "en",

      "curator": "simonebu",

      "collectionCreationDate": 1433424344000,

      "endDate": 1433446767000,

      "startDate": 1433424365000,

      "keywords": "crisis yemen civil war",

      "geo": "41.6082,11.9085,54.7389,19.0",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150615114921_test_-_flood_detection",

      "name": "Test - Flood detection",

      "totalCount": 15645,

      "language": "en",

      "curator": "WmBAnderson",

      "collectionCreationDate": 1434383722000,

      "endDate": 1434547771000,

      "startDate": 1434547194000,

      "keywords": "news,flood,earthquake,help,disaster,emergency",

      "geo": "-132.7,16.8,-63.6,60.9",

      "status": "NOT_RUNNING",

      "labelCount": 25,

      "humanTaggedCount": null

   },

   {

      "code": "150616134403_qatar_charity_during_ramadan",

      "name": "Qatar Charity during Ramadan",

      "totalCount": 418135,

      "language": "en",

      "curator": "HeatherLeson",

      "collectionCreationDate": 1434451605000,

      "endDate": 1439269555000,

      "startDate": 1439237243000,

      "keywords": "charity, #qatar, #charity, #Ramadan2015, qatar, #doha",

      "geo": "50.5675,24.4708,52.638,26.383",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150623153737_demo_for_alt",

      "name": "Demo for ALT",

      "totalCount": 60350,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1435063140000,

      "endDate": 1435068756000,

      "startDate": 1435063148000,

      "keywords": "uk",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "150624113651_heatwave_pakistan",

      "name": "Heatwave Pakistan",

      "totalCount": 6855,

      "language": "en",

      "curator": "sh_manish",

      "collectionCreationDate": 1435126476000,

      "endDate": 1435172400000,

      "startDate": 1435126496000,

      "keywords": "heatwave pakistan",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150624142601_hurricane_sandy",

      "name": "Hurricane Sandy -test",

      "totalCount": 1830,

      "language": "en",

      "curator": "JSW7565",

      "collectionCreationDate": 1435145446000,

      "endDate": 1435147848000,

      "startDate": 1435146238000,

      "keywords": "isis",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150624154245_",

      "name": "Nepall",

      "totalCount": 241470,

      "language": "en",

      "curator": "ibroso1tan",

      "collectionCreationDate": 1435149819000,

      "endDate": 1435241438000,

      "startDate": 1435149968000,

      "keywords": "Patan, Gorkha, Earthquake, Nepal",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150625220256_pakistan_heatwave_2015",

      "name": "Pakistan Heatwave 2015",

      "totalCount": 21985,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1435259040000,

      "endDate": 1435608071000,

      "startDate": 1435607991000,

      "keywords": "pakistan heatwave, pakistan heat wave, karachi heatwave, karachi heat wave, karachi heat, pakistan heat, pakistan hot summer, pakistan heatwave deaths, heatwave deaths, heatwave dehydration, heatwave emergency, pakistan emergency, pakistan water needs",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 9,

      "humanTaggedCount": null

   },

   {

      "code": "150628133212_test",

      "name": "Kuwait mosque bombing",

      "totalCount": 890,

      "language": "en",

      "curator": "Code_87",

      "collectionCreationDate": 1435484058000,

      "endDate": 1435485831000,

      "startDate": 1435484124000,

      "keywords": "Kuwait bombing, kuwait isis, kuwait masjid, kuwait mosque",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150616124203_download_test",

      "name": "download_test",

      "totalCount": 10165,

      "language": "en,fr,es",

      "curator": "msinq",

      "collectionCreationDate": 1434447825000,

      "endDate": 1439146800000,

      "startDate": 1439101731000,

      "keywords": "avalanche,landslide,seismic,earthquake,quake,avalanches,landslides,earthquakes,quakes,glissement,effondrement,éboulement,séisme,tremblement,avalanches,glissements,effondrements,éboulements,séismes,tremblements,avalancha,deslizamiento,derrumbe,sismo,temblor,terremoto,avalanchas,deslizamientos,derrumbes,sismos,temblores,terremotos",

      "geo": "139.04,32.81,139.88,33.65",

      "status": "TRASHED",

      "labelCount": 17,

      "humanTaggedCount": null

   },

   {

      "code": "150704160754_desplazamiento",

      "name": "desplazamiento Colombia",

      "totalCount": 6460,

      "language": "en,es",

      "curator": "Ascapa01",

      "collectionCreationDate": 1436022602000,

      "endDate": 1437389789000,

      "startDate": 1437382537000,

      "keywords": "#mataron,#sacaron,#familia,#Villavo,#capital,#tierra,#uniformados",

      "geo": "-74.9028,2.6277,-71.0699,4.3157",

      "status": "TRASHED",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150707142337_vyapam_scam",

      "name": "Vyapam_scam",

      "totalCount": 92420,

      "language": "en",

      "curator": "GoyalKushalKant",

      "collectionCreationDate": 1436259246000,

      "endDate": 1437285606000,

      "startDate": 1437111823000,

      "keywords": "#vyapam, #mp, #india",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150707175747_",

      "name": "cincinnati shootings",

      "totalCount": 0,

      "language": "en",

      "curator": "cjprofman",

      "collectionCreationDate": 1436306369000,

      "endDate": 1436871603000,

      "startDate": 1436698304000,

      "keywords": "#shootings #violence #cpd #crime #thug #police",

      "geo": "-84.71239,39.052056,-84.368778,39.221037",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150708100020_charity_data_in_qatar",

      "name": "Charity Data in Qatar",

      "totalCount": 4963600,

      "language": "en,",

      "curator": "Catherineleson",

      "collectionCreationDate": 1436339549000,

      "endDate": 1440651578000,

      "startDate": 1440651569000,

      "keywords": "iftar, ramadan, ramadankareem, charity, doha, qatar,Qatar Charity, eid charity, rota, reach out to asia, Sheikh Thani Bin Abdullah Foundation,qatar red crescent society, poor, give, donate, donor, donation, aid, assist, collection box, charitable, relief, sponsor, volunteer, humanitarian, gift, aid, outreach, endow, endowment, community service, service, assistance, suhoor, tent, ramadan tent, ramadan car parade, car parade, katara, fireworks, bazaar, qrcs",

      "geo": "50.6993,24.4358,52.5117,26.117",

      "status": "STOPPED",

      "labelCount": 2,

      "humanTaggedCount": null

   },

   {

      "code": "150708180131_nepal_earthquake",

      "name": "Nepal Earthquake 2015_KLL",

      "totalCount": 0,

      "language": "en",

      "curator": "pujan7889556",

      "collectionCreationDate": 1436357926000,

      "endDate": 1436358327000,

      "startDate": 1436357994000,

      "keywords": "earthquake_nepal_2015, nepal_earthquake",

      "geo": "$$c(E 79°57'00\"--E 88°15'00\"/N 30°25'00\"--N 26°21'00\")",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150708180226_nepal_earthquake",

      "name": "earthquake in nepal",

      "totalCount": 35545,

      "language": "en",

      "curator": "Stha_Megha",

      "collectionCreationDate": 1436357800000,

      "endDate": 1436868003000,

      "startDate": 1436692072000,

      "keywords": "nepalearthquake, earthquake",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150708075554_",

      "name": "Cyclone Chan-Hom",

      "totalCount": 3500,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1436356618000,

      "endDate": 1436871607000,

      "startDate": 1436698065000,

      "keywords": "Cyclone chan, cyclone hom, cyclone chan-hom, chan-hom",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150716160024_india",

      "name": "India",

      "totalCount": 117985,

      "language": "en,hi",

      "curator": "latika1912",

      "collectionCreationDate": 1437031871000,

      "endDate": 1446886730000,

      "startDate": 1446886913000,

      "keywords": "#india, #bharat",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150716200703_greece",

      "name": "Greece",

      "totalCount": 4270,

      "language": "en",

      "curator": "latika1912",

      "collectionCreationDate": 1437046641000,

      "endDate": 1446725718000,

      "startDate": 1446726950000,

      "keywords": "#greece, #india",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150716214241_us",

      "name": "us",

      "totalCount": 74285,

      "language": "en",

      "curator": "latika1912",

      "collectionCreationDate": 1437052386000,

      "endDate": 1444102449000,

      "startDate": 1444102369000,

      "keywords": "#us",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150717120756_",

      "name": "huge rain storm",

      "totalCount": 7840,

      "language": "en",

      "curator": "nehabansal1010",

      "collectionCreationDate": 1437104309000,

      "endDate": 1437307200000,

      "startDate": 1437131717000,

      "keywords": "rain storm, huge rain",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "150718192143_chilliwack_internet",

      "name": "Vishnu Garden",

      "totalCount": 45,

      "language": "en",

      "curator": "lindeman_3",

      "collectionCreationDate": 1437261831000,

      "endDate": 1437266588000,

      "startDate": 1437265691000,

      "keywords": "#Vishnu, #Garden, #collapse",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150508100801_nepal",

      "name": "nepal",

      "totalCount": 330,

      "language": "en",

      "curator": "hsarmien",

      "collectionCreationDate": 1431090517000,

      "endDate": 1437516723000,

      "startDate": 1437516419000,

      "keywords": "nepal eartquake",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150721220826_",

      "name": "california rainstorm",

      "totalCount": 0,

      "language": "en",

      "curator": "hsarmien",

      "collectionCreationDate": 1437516697000,

      "endDate": 1437858003000,

      "startDate": 1437684061000,

      "keywords": "#california #rainstorm #rain",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150722164310_fukushima",

      "name": "radiation",

      "totalCount": 730,

      "language": "en",

      "curator": "PPloveKK",

      "collectionCreationDate": 1437540262000,

      "endDate": 1437541338000,

      "startDate": 1437540988000,

      "keywords": "Japan",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 24,

      "humanTaggedCount": null

   },

   {

      "code": "150730123331_cyclone_komen_july2015",

      "name": "Cyclone Komen July2015",

      "totalCount": 50865,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1438238187000,

      "endDate": 1441682185000,

      "startDate": 1441187466000,

      "keywords": "komen, cyclone, cyclone bangladesh, cyclone west bengal, bay of bengal, fishermen warning, cyclone mynamar, cyclone myanmar",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "150731141214_landslide_kaski",

      "name": "landslide kaski",

      "totalCount": 0,

      "language": "en",

      "curator": "hsarmien",

      "collectionCreationDate": 1438351997000,

      "endDate": 1438527608000,

      "startDate": 1438352022000,

      "keywords": "kaski masdi landslide",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150803133914_qatar_airways",

      "name": "QA",

      "totalCount": 806080,

      "language": "en",

      "curator": "ibroso1tan",

      "collectionCreationDate": 1438587697000,

      "endDate": 1438761600000,

      "startDate": 1438588737000,

      "keywords": "destination,tickets,expensive,customer,qatarairways,qatar airways,#qatarairways",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 17,

      "humanTaggedCount": null

   },

   {

      "code": "150807192614_santiago_storm",

      "name": "Storm North-Center of Chile",

      "totalCount": 329170,

      "language": "es",

      "curator": "hsarmien",

      "collectionCreationDate": 1438975781000,

      "endDate": 1441998004000,

      "startDate": 1441390806000,

      "keywords": "#lluviatvn,lluvia santiago,valparaiso ,#temporalenmega ,#ovalle, tocopilla, iquique, aconcagua, antofagasta",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 38,

      "humanTaggedCount": null

   },

   {

      "code": "150809105000_twbqcri_trial",

      "name": "TWBQCRI Trial",

      "totalCount": 21532835,

      "language": "en,ar",

      "curator": "Fidget02",

      "collectionCreationDate": 1439104232000,

      "endDate": 1440995018000,

      "startDate": 1440987057000,

      "keywords": "البطن\nحادث , سيارة إسعاف,يبتر , كاحل , حيوان , مضاد حيوي,شقة , ذراع,موجود , يتجنب, بعيداً,طفل , رضيع, ضمادة , حمام , سرير, بطن, ,ميلاد ,بطانية ,نزيف ,كفيف ,ينتفخ ,دم ,جسم ,بثرة – دمل  - يغلي ,خبز , ثدي ,نفس – تنفس ,يتنفس ,جسر ,مكسور ,مبنى ,احتراق - محترق ,حافلة ,سيارة ,صدر , طفل , أطفال , قشعريرة , الكوليرا ,عيادة - مستوصف , مغلق , ملابس ,ا نهار , يتصل , يضغط , ملوث , سعال\nيتحطم , ضرر - خسارة, طبيب ,شرب, على وشك الموت, زلزال, كوع - مرفق ,كهل ,- مسن ,كهربائي ,طوارئ ,يتغوط ,انفجار ,تعرض ,وجه ,أقدام ,حمى ,نار ,إسعاف ,أولي ,مشاعل لهب ,فيضان ,سائل ,طعام ,قدم ,ساعد - ذراع\nوقود ,أدخنة - أبخرة ,غاز - بنزين ,حكومة ,أصل الفخذ – بداية الفخذ ,حراس ,بندقية – ,مسدس - طبنجة ,طلقات نارية ,يد ,خطر ,رأس ,صداع ,صحة ,مساعدة ,عال – مرتفع ,مستشفى ,فندق ,منزل ,جائع ,يجرح - أذى ,كوخ ,مرض – داء ,معد ,مجروح ,الانترنت  يعزل ,ركبة ,انزلاق أرضي ,مرحاض ,أيسر - يسار\n ,ساق - رجل ,خط ,مفقود ,منخفض ,مركز تجاري ,رجل ,دواء ,طبيب ,علاج ,عسكري ,حليب - لبن ,مفقود ,جماهير - غوغاء ,أم ,فم ,انزلاق طيني ,رقبة ,حاجة ,مواليدي - وليدي  ,لا ,ممرضة ,خارجي ,في الخارج ,أكسجين ,ألم ,مؤلم ,كف – راحة ,اليد ,شخص ,هاتف – تليفون ,طائرة ,من فضلك ,الشرطة – البوليس ,صالح للشرب ,قوة ,حامل (المرأة) ,حماية ,نبض ,مرض الكلب ,مطر ,مغتصبة ,طفح جلدي - متهور ,ينقذ ,أرز ,يمين (ناحية) ,نهر ,طريق ,سقف ,حطام ,مدرج هبوط - مجرى ,مدرسة ,صرخة ,بحر ,يبحث ,أمن ,يرى ,خدمة ,يهز ,لوح ,مأوى ,سفينة ,صدمة ,طلقة – أطلق ,الرصاص ,كتف ,مريض ,يشم ,دخان ,العمود الفقري ,غرز ,معدة – بطن ,براز – ,غائط ,عاصفة ,شارع ,ملصق ,اختناق ,سكر ,يوفر – مؤونة ,جراحة ,خيوط الجراحة ,دوار - سباحة ,عرض – علامة ,درجة الحرارة ,فخذ ,خيام ,هم – هن - هما ,هؤلاء ,قطار ,نقل ,وقع في شرك - أعيق ,قمامة – نفاية ,يسافر ,شجرة ,غطاء شاحنة ,تسونامي – فيضان ضخم ,بول ,تحذير ,مياه ,موجة ,ضعيف ,سلاح ,من – الذي ,أين ,امرأة ,رسغ – معصم",

      "geo": "41.82,12.11,54.53,19.0",

      "status": "TRASHED",

      "labelCount": 17,

      "humanTaggedCount": null

   },

   {

      "code": "150809125803_poaching_in_zimbabwe",

      "name": "Poaching in Zimbabwe",

      "totalCount": 128055,

      "language": "en",

      "curator": "Tim_S_Wilkinson",

      "collectionCreationDate": 1439110706000,

      "endDate": 1439271644000,

      "startDate": 1439183617000,

      "keywords": "poaching Zimbabwe, hunting Zimbabwe, lions, tigers, elephants, rhino, rhino's",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150811110148_worde_tranlsation_-_alphabet",

      "name": "WoRDE translation - alphabet",

      "totalCount": 644045,

      "language": "en",

      "curator": "HeatherLeson",

      "collectionCreationDate": 1439269550000,

      "endDate": 1439352250000,

      "startDate": 1439349398000,

      "keywords": "google, alphabet, abc, larry brin, Sergey Brin, eric schimdt, Sundar Pichai,chrome, android, nest, loon, from a to z,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 2,

      "humanTaggedCount": null

   },

   {

      "code": "150811085839_ferguson",

      "name": "ferguson test",

      "totalCount": 2781090,

      "language": "en",

      "curator": "mava_mack",

      "collectionCreationDate": 1439269607000,

      "endDate": 1439352256000,

      "startDate": 1439347730000,

      "keywords": "ferguson, Michael Brown, august, september, 2015, coffee, tea, hello,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150811141632_alphabet",

      "name": "Alphabet",

      "totalCount": 717430,

      "language": "en",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1439272175000,

      "endDate": 1446982420000,

      "startDate": 1446983083000,

      "keywords": "Alphabet, alphabet, google, Sundar Pichai, Larry Page, Sergey Brin,Pichai",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150818121051_test_fast_collection",

      "name": "Test Fast Collection",

      "totalCount": 61626045,

      "language": "en",

      "curator": "kushalkantgoyal",

      "collectionCreationDate": 1439869316000,

      "endDate": 1445333532000,

      "startDate": 1445326655000,

      "keywords": "ambulance, mutilated, ankle, animal, antibiotic, apartment, arm, is, avoid, away, baby, baby, bandage, bath, bed, belly, birth, blanket, bleeding, blind, bulging, blood, body, pimple - boil - boil, bake, breast, breathe, bridge, broken, building, burning, bus, car, chest, child, children, chills, cholera, clinic, closed, clothes, contaminated, cough crashes, damage, doctor",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150831095234_qcri_-_demo",

      "name": "QCRI - Demo",

      "totalCount": 5881935,

      "language": "en,ar",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1440993201000,

      "endDate": 1441683967000,

      "startDate": 1441679296000,

      "keywords": "war, البطن\nحادث , سيارة إسعاف,يبتر , كاحل , حيوان , مضاد حيوي,شقة , ذراع,موجود , يتجنب, بعيداً,طفل , رضيع, ضمادة , حمام , سرير, بطن, ,ميلاد ,بطانية ,نزيف ,كفيف ,ينتفخ ,دم ,جسم ,بثرة – دمل  - يغلي ,خبز , ثدي ,نفس – تنفس ,يتنفس ,جسر ,مكسور ,مبنى ,احتراق - محترق ,حافلة ,سيارة ,صدر , طفل , أطفال , قشعريرة , الكوليرا ,عيادة - مستوصف , مغلق , ملابس ,ا نهار , يتصل , يضغط , ملوث , سعال\nيتحطم , ضرر - خسارة, طبيب ,شرب, على وشك الموت, زلزال, كوع - مرفق ,كهل ,- مسن ,كهربائي ,طوارئ ,يتغوط ,انفجار ,تعرض ,وجه ,أقدام ,حمى ,نار ,إسعاف ,أولي ,مشاعل لهب ,فيضان ,سائل ,طعام ,قدم ,ساعد - ذراع\nوقود ,أدخنة - أبخرة ,غاز - بنزين ,حكومة ,أصل الفخذ – بداية الفخذ ,حراس ,بندقية – ,مسدس - طبنجة ,طلقات نارية ,يد ,خطر ,رأس ,صداع ,صحة ,مساعدة ,عال – مرتفع ,مستشفى ,فندق ,منزل ,جائع ,يجرح - أذى ,كوخ ,مرض – داء ,معد ,مجروح ,الانترنت  يعزل ,ركبة ,انزلاق أرضي ,مرحاض ,أيسر - يسار\n ,ساق - رجل ,خط ,مفقود ,منخفض ,مركز تجاري ,رجل ,دواء ,طبيب ,علاج ,عسكري ,حليب - لبن ,مفقود ,جماهير - غوغاء ,أم ,فم ,انزلاق طيني ,رقبة ,حاجة ,مواليدي - وليدي  ,لا ,ممرضة ,خارجي ,في الخارج ,أكسجين ,ألم ,مؤلم ,كف – راحة ,اليد ,شخص ,هاتف – تليفون ,طائرة ,من فضلك ,الشرطة – البوليس ,صالح للشرب ,قوة ,حامل (المرأة) ,حماية ,نبض ,مرض الكلب ,مطر ,مغتصبة ,طفح جلدي - متهور ,ينقذ ,أرز ,يمين (ناحية) ,نهر ,طريق ,سقف ,حطام ,مدرج هبوط - مجرى ,مدرسة ,صرخة ,بحر ,يبحث ,أمن ,يرى ,خدمة ,يهز ,لوح ,مأوى ,سفينة ,صدمة ,طلقة – أطلق ,الرصاص ,كتف ,مريض ,يشم ,دخان ,العمود الفقري ,غرز ,معدة – بطن ,براز – ,غائط ,عاصفة ,شارع ,ملصق ,اختناق ,سكر ,يوفر – مؤونة ,جراحة ,خيوط الجراحة ,دوار - سباحة ,عرض – علامة ,درجة الحرارة ,فخذ ,خيام ,هم – هن - هما ,هؤلاء ,قطار ,نقل ,وقع في شرك - أعيق ,قمامة – نفاية ,يسافر ,شجرة ,غطاء شاحنة ,تسونامي – فيضان ضخم ,بول ,تحذير ,مياه ,موجة ,ضعيف ,سلاح ,من – الذي ,أين ,امرأة ,رسغ – معصم",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150831135438_demo_tweets",

      "name": "Demo tweets",

      "totalCount": 91930,

      "language": "en,ar",

      "curator": "jikimlucas",

      "collectionCreationDate": 1441007732000,

      "endDate": 1446096890000,

      "startDate": 1446096822000,

      "keywords": "war, people, crime, shelter, food, money donation",

      "geo": "9.3232,46.2237,13.9582,48.475",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150902142649_migrant_crisis",

      "name": "Migrant Crisis",

      "totalCount": 2279470,

      "language": "",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1441182437000,

      "endDate": 1441617383000,

      "startDate": 1441616387000,

      "keywords": "refugees, refugee, syria, Syrian, Germany, german, Budapest, Hungary,Migrant Crisis",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150903091540_frontera_colombia-venezuela",

      "name": "Frontera Colombia-Venezuela",

      "totalCount": 47010,

      "language": "en,es",

      "curator": "kublaykan",

      "collectionCreationDate": 1441279708000,

      "endDate": 1442671201000,

      "startDate": 1442065801000,

      "keywords": "#AtenciónFrontera, #VzlaEjemploDePaz, #ETCrisisVenezuela, #Frontera, #Todosenlafrontera, #Crisisenlafrontera, #Venezuelavaporlapaz, #CrisisFronteriza, #RCNenlaFrontera, #SomosFrontera, #SOSFrontera",

      "geo": "-74.42,1.0,-66.74,12.59",

      "status": "STOPPED",

      "labelCount": 32,

      "humanTaggedCount": null

   },

   {

      "code": "150903122523_suffolk_county_marathon",

      "name": "Suffolk County Marathon",

      "totalCount": 39855,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1441287310000,

      "endDate": 1442289605000,

      "startDate": 1442289604000,

      "keywords": "Suffolk, Old Place Path,  Heckscher Pkwy, Connetquot Avenue, Boyard Cutting Arboretum, Sunrise Business Center,Sunrise Hwy, Idlhour Blvd, Biltmore Ave, Vanderbilt Blvd, Montauk Hwy, Oakdale Ave, West Shore Road, Lincoln Drive, Oakdale Bohemia Road and Montauk Hwy,\nBridle Way and Montauk Hwy,\nBayview Drive and Montauk Hwy,\nEast Shore Road and Montauk Hwy,\nOckers Garden Apartments,\nCanterbury Court and Montauk Hwy,\nLighthouse Commons Shopping Center,\nBerard Blvd and Montauk Hwy,\nSt. John's University,\nLocust Ave and Montauk Hwy,\nBrook Street and Montauk Hwy,\nLaSalle Place and Montauk Hwy,\nLaSalle Commons Entrance and Exit,\nWest Oake Private Club,\nDale Drive and Montauk Highway,\nBetsy Drive and Montauk Highway,\nMunson Lane and Montuak Highway,\nColony Drive and Montauk Highway,\nWashington Ave and Montauk Highway,\nRollstone Ave and Montauk Hwy,\nWest Ave and Montauk Hwy,\nTyler Ave / Atlantic Ave and Montauk Hwy,\nCherry Avenue and Montauk Hwy,\nWest Lane and Main Street,\nBaymen's Street and Main Street,\nSunset Drive and Main Street,\nBenson Ave / Saxon Ave and Main,\nGarfield Ave and Main Street,\nHandsome Ave and Main Street,\nGreeley Ave and Main Street,\nGreene Ave and Main Street,\nCandee Ave and Main Street,\nLakeland Ave / Gillette Ave and Main Street,\nLincoln Ave and Main Street,\nFoster Ave and Middle Road,\nHiddink Street and Montauk Hwy,\nHanson Place and Montauk Hwy,\nSejon and Montauk Hwy,\nMillpond Road and Montauk Hwy,\nSayville Blvd and Montauk Hway,\nSeville Blvd and Montauk Hwy,\nLowell Road and Montauk Hwy,\nAmy Drive and Montauk Hwy,\nBroadway Ave and Montauk Hwy,\nOld Broadway Ave and Montauk Hwy,\nMcConnell Ave and Montauk Hwy,\nLakeview Ave / Oakwood Ave and Montauk Hwy,\nBernice Drive and Montauk Hwy,\nThird Ave and Montauk Hwy,\nSnedecor Ave and Montauk Hwy,\nSecond Ave and Montauk Hwy,\n1st Ave and Montauk Hwy,\nBayport Ave and Montauk Hwy,\nBarrett Ave and Montauk Hwy,\nSylvan Ave and Montauk Hwy,\nGillette Ave and Montauk Hwy,\nNicolls Road and Purick,\nNicolls Road and Church,\nBuffin Street and Montauk Hwy,\nBell Ave and Montauk Hwy,\nBlue Point Ave and Montauk Hwy,\nBrowne Ave and Montauk Hwy,\nBrook Ave/Kennedy Ave and Montauk Hwy,\nHillside Ave and Montauk Hwy,\nDivision Ave and Montauk Hwy,\nPleasant Ave and Montauk Hwy,\nHoman Ave and Rolland Ave,\nAtlantic Ave and Montauk Hwy,\nNorthwood Lane and Montauk Hwy,\nBianca Road and Montauk Hwy,\nHighland Ave and Montauk Hwy,\nN/S Prospect and Montauk Hwy,\nNorth Summit and Montauk Hwy,\nSouth Summit and Montauk Hwy,\nLakeland Ave and West Main,\nWaterworks Lane and Main Street,\nWest Lake Drive and Main Street,\nWaverly Ave and Main Street,\nRiver Ave and Main Street,\nYMCA/Briarcliff College Entrance/Exit,\nWest Ave and Veterand Blvd,\nWest Ave and Holbrook Road / Lake Street,\nWest Ave and South Street,\nRailroad Ave and Church Street,\nRailroad Ave and Rialto Way,\nRailraod Ave and Gerard Street,\nChurch Street and South Ocean Ave,\nSouth Ocean Ave and Terry Street,\nMontauk Highway and Maple Ave,\nNorth Ocean Ave and Oak Street, Marathon accident,\nMarathon ambulance,\nMarathon bleeding,\nMarathon bleeding,\nMarathon bomb,\nMarathon breathing,\nMarathon dead,\nMarathon death,\nMarathon explosion,\nMarathon faint,\nMarathon fall,\nMarathon fight,\nMarathon fire,\nMarathon heart attack,\nMarathon heat,\nMarathon hurt,\nMarathon injury,\nMarathon over heat,\nMarathon passed out,\nMarathon poison,\nMarathon shooting,\nMarathon sick,\nMarathon stabbing,\nMarathon suspicous package,\nMarathon unattended,\nMarathon stroke,\nMarathon terror,\nMarathon terrorism,\nMarathon weapon,\nMarathon hazard,\nMarathon hazardous\nMarathon backpack,\nMarathon mob,\nMarathon angry,\nMarathon planted,\nMarathon hide,\nMarathon hidden,\nMarathon sabotage,\nMarathon plot,\nMarathon conspiracy,\nMarathon target,\nMarathon ground zero,\nMarathon protest,\nMarathon chemical,\nMarathon contaminate,\nMarathon infectious,\nMarathon al qaeda,\nMarathon isis,\nMarathon domestic,\nMarathon criminal,\nMarathon assault,\nMarathon police,\nMarathon pd,\nMarathon 50,\nMarathon five-o,\nMarathon help,\nMarathon danger,\nMarathon dangerous,\nMarathon source,\nMarathon ignition,\nMarathon detonate,\nMarathon dirty bomb,\nMarathon drug,\nMarathon medical,\nMarathon medicine,\nMarathon disrupt,\nMarathon casualty,\nSuffolk Marathon,\nSC Marathon,\nMarathon runner injury,\nMarathon First Responder,\nMarathon Security,\nMarathon Incident,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150908094606_qcri-sms-demo",

      "name": "QCRI-SMS-DEMO",

      "totalCount": 89115,

      "language": null,

      "curator": "jlucasQatar",

      "collectionCreationDate": 1441684019000,

      "endDate": 1446171367000,

      "startDate": 1446002138000,

      "keywords": null,

      "geo": null,

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150909145335_suffolk_marathon_9-19_test",

      "name": "Suffolk Marathon 9-19 test",

      "totalCount": 2690,

      "language": "en",

      "curator": "fresfr27",

      "collectionCreationDate": 1441814059000,

      "endDate": 1441899001000,

      "startDate": 1441820354000,

      "keywords": "Suffolk Marathon, Suffolk County Marathon, Marathon Runners, Suffolk Injury, Suffolk Marathon Accident, SC Marathon, Suffolk County, Suffolk Co,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 38,

      "humanTaggedCount": null

   },

   {

      "code": "150910103159_tag_der_patrioten",

      "name": "Tag der Patrioten",

      "totalCount": 28900,

      "language": "en,de",

      "curator": "Stefan_Martini",

      "collectionCreationDate": 1441863352000,

      "endDate": 1442123985000,

      "startDate": 1442056213000,

      "keywords": "Tag der Patrioten, 1209HH, Demo Hamburg, Hamburgregeltdas‬, nonazisHH, #fcknzs, #HoGeSa, GFD, HH1209, #hetzjagdaufnazis,‪#‎mobwatch‬,#1209tddp, #1209hbinfo, #1209hb, #Kirchweyhe",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "150910143013_suffolk_county_flooding",

      "name": "Suffolk County Flooding",

      "totalCount": 3920,

      "language": "en",

      "curator": "fresfr27",

      "collectionCreationDate": 1441899305000,

      "endDate": 1442095200000,

      "startDate": 1441922288000,

      "keywords": "Floods, Flooding, Damage, Road Closed, help, flash flood, warning, flooded roads, Suffolk county, Southern State Pkwy, Rt 231, Sagtikos, #everythingisclosed, #SuffolkFlood, Crooked Hill Rd, submerged vehicle, Lincoln Dr, Lightening, power outage, heavy rain, Sunrise Highway, Flood, Road Flooding, LIRR, accident, disabled vehicle, Street Flooding, submerged, Water, drainage, stranded, storm, side streets, record setting, inundation, Soaks, Rain, advisory, NWS ,showers, Thunderstorms, Watch, #flooding,Long Island, Radar. Weather, precipitation, hazard, heavy, dangerous",

      "geo": "-73.424697,40.626908,-71.802198,41.183053",

      "status": "TRASHED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150910120555_syrian_refugees",

      "name": "Syrian Refugees",

      "totalCount": 38834605,

      "language": "en,ar",

      "curator": "JoyceMonsees",

      "collectionCreationDate": 1441901512000,

      "endDate": 1443181069000,

      "startDate": 1443072969000,

      "keywords": "Hungary, border, need, need blanket, sanitary, tent, help, dead, injured, Syria, refugee,",

      "geo": "3.8,32.76,38.75,53.27",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "150912210152_tw",

      "name": "TW",

      "totalCount": 8980545,

      "language": "ar,hu",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1442070407000,

      "endDate": 1442563201000,

      "startDate": 1442388829000,

      "keywords": "الطرد, الترحيل، الإخراج,العائلة,خائف, خائفة, خائفين,حق اللجوء السياسي,لاجئ، اللاجئين, هنغاريا,صربيا,النمسا,ألمانيا,سوريا,الحرية,السلام, Száműz\nKitelepítés, Család, Rémült, Menedékhely, Menekült, Magyarország, Szerbia, Ausztria, Németország, Szíria, Szabadság, Béke, röszke,  határ, migránsok, határzár,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150913121907_suffolk_county_marathon_-_jus_new_one",

      "name": "Suffolk County Marathon - Jus new one",

      "totalCount": 8945,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1442132718000,

      "endDate": 1442242033000,

      "startDate": 1442134084000,

      "keywords": "longisland, #suffolk, #suffolkmarathon, suffolk marathon, long island, suffolkcountymarathon, #suffolkcountymarathon, Montauk Highway, Heckscher State Park, Patchogue, oakdale, sayville, bay port, blue point, scpd, scrunner, suffolkrace",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150914012314_cizre_under_attack",

      "name": "Cizre Under Attack",

      "totalCount": 2440,

      "language": "en,tr",

      "curator": "Reddit_Privacy",

      "collectionCreationDate": 1442172278000,

      "endDate": 1442330846000,

      "startDate": 1442172710000,

      "keywords": "#cizreunderattack",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150915111643_orop",

      "name": "OROP",

      "totalCount": 120,

      "language": "en",

      "curator": "msinq",

      "collectionCreationDate": 1442294266000,

      "endDate": 1442469853000,

      "startDate": 1442469742000,

      "keywords": "India,OROP",

      "geo": "66.86,8.07,97.86,37.49",

      "status": "TRASHED",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "150915120111_",

      "name": "Boston Riots",

      "totalCount": 76165,

      "language": "en",

      "curator": "hsjomaa",

      "collectionCreationDate": 1442296878000,

      "endDate": 1442473200000,

      "startDate": 1442300392000,

      "keywords": "brutality, damage, raids",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150916150738_",

      "name": "Japan typhoon etau",

      "totalCount": 30445,

      "language": "en",

      "curator": "aidr_test",

      "collectionCreationDate": 1442373047000,

      "endDate": 1442548812000,

      "startDate": 1442373070000,

      "keywords": "etau, japan typhoon, flooding, rainfall, tropical storm",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "150917034553_chile_eart",

      "name": "Chile Earthquake",

      "totalCount": 1200860,

      "language": "es,en",

      "curator": "Reddit_Privacy",

      "collectionCreationDate": 1442440234000,

      "endDate": 1443272646000,

      "startDate": 1443118367000,

      "keywords": "#chile, chile earthquake, earthquake chile, chile tsunami, #TerremotoChile, terremoto",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150916200834_chile_earthquake",

      "name": "Chile Earthquake 9-2015",

      "totalCount": 315630,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1442438009000,

      "endDate": 1443802607000,

      "startDate": 1443527474000,

      "keywords": "chile earthquake, peru earthquake, hawaii earthquake, chile tsunami, peru tsunami, hawaii tsunami, magnitude 8.3, magnitude 7.9, M 8.3, M 7.9, Illapel tsunami, Illapel earthquake, earthquake help, tsunami help, #Chile, #ChileEarthquake,  #earthquake, #terremoto, #ChileQuake, #antofagasta,  #Concón,  #Valparaiso,  #Illapel",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "150916232031_earthquake_north_of_chile",

      "name": "Earthquake north of chile",

      "totalCount": 1846020,

      "language": "en,es",

      "curator": "hsarmien",

      "collectionCreationDate": 1442445797000,

      "endDate": 1442850741000,

      "startDate": 1442445819000,

      "keywords": "#terremoto, terremoto, norte, chile, #chile, #norte, #temblor, temblor, #onemi, onemi, #terremotochile, #terremototvn, #tsunami",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150921130956_bf",

      "name": "BF",

      "totalCount": 80995,

      "language": "en,fr",

      "curator": "Fidget02",

      "collectionCreationDate": 1442827000000,

      "endDate": 1443160539000,

      "startDate": 1443118968000,

      "keywords": "Burkina faso coup, demonstration coup, Ouagadougou, burkina RSP, coup d'etat, #burkinafaso, burkina faso, #Ouagadougou burkina RSP, #lwili, lwili,",

      "geo": "-5.52,9.41,2.4,15.08",

      "status": "NOT_RUNNING",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "150922025921_",

      "name": "namarotaib",

      "totalCount": 0,

      "language": null,

      "curator": "13v11",

      "collectionCreationDate": 1442869341000,

      "endDate": 1442869836000,

      "startDate": 1442869364000,

      "keywords": null,

      "geo": null,

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150925152924_food_insecurity_in_yemen",

      "name": "Food security in Yemen",

      "totalCount": 1248570,

      "language": "en",

      "curator": "HagarP7",

      "collectionCreationDate": 1443177144000,

      "endDate": 1444474800000,

      "startDate": 1444300334000,

      "keywords": "conflict, war, markets, food commodities, non-food commodities, bread, food crop, food transportation, fuel, diesel, water, food shortage, shortage, market closure, food supply, supply chain, food traders, bread, rice, wheat, meat, fish, eggs, cereals, staples, food harvest, limited food, displaced, infant milk, food assistance, food aid, World food programme, electricity, cooking gas, food unavailability, food access, famine, blockade, food insecurity, borrow food, food consumption, food needs, market closure, food monopoly, agriculture",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150925155258_yemen_conflict",

      "name": "Yemen Crisis",

      "totalCount": 34210,

      "language": "en,ar",

      "curator": "HagarP7",

      "collectionCreationDate": 1443179385000,

      "endDate": 1444300242000,

      "startDate": 1444298906000,

      "keywords": "صراع, حرب, الأسواق, السلع الغذائية, السلع غير الغذائية, خبز, المحاصيل الغذائية, نقل الأغذية, وقود, ديزل, ماء, نقص الغذاء, نقص, إغلاق السوق, الإمدادات الغذائية, الموردين, تجار المواد الغذائية, خبز, الأرز, قمح, لحم, سمك, بيض, حبوب, المواد الغذائية, حصاد الغذائية,الغذاء محدود, النازحين, حليب الرضع, مساعدات غذائية,المساعدات الغذائية, برنامج الغذاء العالمي, كهرباء, غاز الطبخ, عدم توفر الغذاء, الحصول على الغذاء, مجاعة, حصار, انعدام الأمن الغذائي, الاقتراض الطعام, استهلاك الطعام, الاحتياجات الغذائية, إغلاق السوق,احتكار المواد الغذائية, الزراعةال ,من,الغذاء اليمن ,الماء اليمن, الكهرباء اليمن, الغاز اليمن, الزراعة اليمن, المحاصيل اليمن, الحصاد اليمن, الخبز اليمن الحليب",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151001224605_hurricane_joaquin",

      "name": "Hurricane Joaquin",

      "totalCount": 260820,

      "language": "en",

      "curator": "VLiliann",

      "collectionCreationDate": 1443718109000,

      "endDate": 1443891600000,

      "startDate": 1443718130000,

      "keywords": "Hurricane Joaquin, Joaquin, HurricaneJoaquin",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151016031614_airstrike",

      "name": "airstrike",

      "totalCount": 309065,

      "language": "en",

      "curator": "ElenCrystal",

      "collectionCreationDate": 1444947337000,

      "endDate": 1445230801000,

      "startDate": 1445056188000,

      "keywords": "airstrikes, bombings, destroyed, isis",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 4,

      "humanTaggedCount": null

   },

   {

      "code": "151008192345_yemen123",

      "name": "Yemen123",

      "totalCount": 125055,

      "language": "en,ar",

      "curator": "latika1912",

      "collectionCreationDate": 1444301646000,

      "endDate": 1444985778000,

      "startDate": 1447135451000,

      "keywords": "صراع, حرب, الأسواق, السلع الغذائية, السلع غير الغذائية, خبز, المحاصيل الغذائية, نقل الأغذية, وقود, ديزل, ماء, نقص الغذاء, نقص, إغلاق السوق, الإمدادات الغذائية, الموردين, تجار المواد الغذائية, خبز, الأرز, قمح, لحم, سمك, بيض, حبوب, المواد الغذائية, حصاد الغذائية,الغذاء محدود, النازحين, حليب الرضع, مساعدات غذائية,المساعدات الغذائية, برنامج الغذاء العالمي, كهرباء, غاز الطبخ, عدم توفر الغذاء, الحصول على الغذاء, مجاعة, حصار, انعدام الأمن الغذائي, الاقتراض الطعام, استهلاك الطعام, الاحتياجات الغذائية, إغلاق السوق,احتكار المواد الغذائية, الزراعة",

      "geo": "",

      "status": "RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151016143006_hurricane_sandy",

      "name": "South Carolina Flood SAMPLE",

      "totalCount": 0,

      "language": "en",

      "curator": "PA_MediaMonitor",

      "collectionCreationDate": 1445009667000,

      "endDate": 1445011140000,

      "startDate": 1445011041000,

      "keywords": "#shelter, #redcross, #flooding",

      "geo": "-82.3601,32.1133,-78.2732,35.1064",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151016120222_typhoon_koppu-lando_oct_2015",

      "name": "Typhoon Koppu-Lando Oct 2015",

      "totalCount": 1495,

      "language": "en",

      "curator": "JoyceMonsees",

      "collectionCreationDate": 1445011609000,

      "endDate": 1445561929000,

      "startDate": 1445322753000,

      "keywords": "#Koppu, #LandoPH, #typhoon Koppu, #typhoon Lando, #BePreparedPH, #tifon, #tifónKoppu, #LandoPH manila, #LandoPH metro manila, Lando, Koppu",

      "geo": "119.3555,13.8807,122.8271,19.5805",

      "status": "NOT_RUNNING",

      "labelCount": 27,

      "humanTaggedCount": null

   },

   {

      "code": "151016151123_harrisburg_traffic_sample",

      "name": "Harrisburg Traffic SAMPLE",

      "totalCount": 205,

      "language": "en",

      "curator": "PA_MediaMonitor",

      "collectionCreationDate": 1445012065000,

      "endDate": 1445497203000,

      "startDate": 1445322753000,

      "keywords": "#Harrisburg, #PennDOT, #PADEP, Harrisburg, accident, traffic",

      "geo": "-77.597,39.6655,-76.0106,40.7046",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "151019150023_vyapam_scam",

      "name": "Vyapam Scam",

      "totalCount": 130,

      "language": "en",

      "curator": "sushant_dahiya",

      "collectionCreationDate": 1445236251000,

      "endDate": 1445893200000,

      "startDate": 1445921138000,

      "keywords": "#vyapamscam",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "151022205934_hurricane_patricia_october_2015",

      "name": "Hurricane Patricia October 2015",

      "totalCount": 979010,

      "language": "en",

      "curator": "JoyceMonsees",

      "collectionCreationDate": 1445562973000,

      "endDate": 1445920607000,

      "startDate": 1446121943000,

      "keywords": "#Patricia, PACcoast, Jalisco, PuertoVallarta, Colima, Nayarit, Huracán, HuracanPatricia, Huracan, categoria5, HurricanePatricia, #Perula, #SanPatricio, #RefugiosTemporales, #PrevenirEsVivir, Patricia, hurricane patricia,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 10,

      "humanTaggedCount": null

   },

   {

      "code": "151025125922_hogesa",

      "name": "HoGeSa",

      "totalCount": 21705,

      "language": "en,de",

      "curator": "Stefan_Martini",

      "collectionCreationDate": 1445763667000,

      "endDate": 1445846935000,

      "startDate": 1445766120000,

      "keywords": "#HoGeSa, HogeSa, koeln2510, #Nohogesa",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 15,

      "humanTaggedCount": null

   },

   {

      "code": "151025201406_elecciones_venezuela",

      "name": "elecciones Venezuela",

      "totalCount": 5113425,

      "language": "en,es",

      "curator": "chaferox",

      "collectionCreationDate": 1445818569000,

      "endDate": 1446094800000,

      "startDate": 1445920973000,

      "keywords": "elecciones venezuela, parlamentarias 2015, 6D, buscar votos, asamblea nacional, AN, CNE",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151026132519_earthquake-af-pk-in",

      "name": "Earthquake-AF-PK-IN 2015",

      "totalCount": 259560,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1445844564000,

      "endDate": 1446094806000,

      "startDate": 1445921356000,

      "keywords": "earthquake Pakistan, earthquake Afghanistan, earthquake India, #earthquake, #PrayForPakistan, #PrayForAfghanistan, #PrayForIndia, earthquake Kashmir",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 14,

      "humanTaggedCount": null

   },

   {

      "code": "151026120622_earthquake_pakistan",

      "name": "Earthquake Pakistan",

      "totalCount": 156045,

      "language": "en",

      "curator": "Stefan_Martini",

      "collectionCreationDate": 1445846922000,

      "endDate": 1446296400000,

      "startDate": 1446121943000,

      "keywords": "earthquake pakistan,",

      "geo": "65.92,31.73,77.61,38.1",

      "status": "STOPPED",

      "labelCount": 21,

      "humanTaggedCount": null

   },

   {

      "code": "151101082341_emsc",

      "name": "EMSC-Geo",

      "totalCount": 0,

      "language": "en,fr,es",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1446344759000,

      "endDate": 1446519601000,

      "startDate": 1446344780000,

      "keywords": "avalanche,landslide,seismic,earthquake,quake,avalanches,landslides,earthquakes,quakes,glissement,effondrement,éboulement,séisme,tremblement,avalanches,glissements,effondrements,éboulements,séismes,tremblements,avalancha,deslizamiento,derrumbe,sismo,temblor,terremoto,avalanchas,deslizamientos,derrumbes,sismos,temblores,terremotos",

      "geo": "-76.56,-14.70,-75.82,-13.96,-73.95,-39.38,-72.69,-38.12,175.32,-40.04,176.58,-38.78",

      "status": "STOPPED",

      "labelCount": 3,

      "humanTaggedCount": null

   },

   {

      "code": "151101124733_cyclone_chapala_-_arabic",

      "name": "Cyclone Chapala - Ar",

      "totalCount": 138395,

      "language": "ar",

      "curator": "MemoAlhamadi",

      "collectionCreationDate": 1446360669000,

      "endDate": 1446537763000,

      "startDate": 1446535685000,

      "keywords": "#سلطنة ـ عمان , #شابالا ,  إعصار شابالا , #اعصارـ شابالا , #شابالاـ عمان , #ظفار , #متابعةـبحرـالعرب , بحر العرب , #حضرموت , #اعصار , #طقسـالعرب , #شرورةـ الآن , #الخرير, #المكلا , #شبوة , #سقرطى , #المهرة ,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 7,

      "humanTaggedCount": null

   },

   {

      "code": "151103123915_yemen_arabic",

      "name": "YEMEN ARABIC",

      "totalCount": 3165,

      "language": "en,ar",

      "curator": "HagarP7",

      "collectionCreationDate": 1446540002000,

      "endDate": 1446554140000,

      "startDate": 1446554428000,

      "keywords": "صراع, حرب, الأسواق, السلع الغذائية, السلع غير الغذائية, سعر الخبز, المحاصيل الغذائية, نقل الأغذية, وقود, ديزل, ماء, نقص الغذاء, نقص, إغلاق السوق, الإمدادات الغذائية, الموردين, تجار المواد الغذائية, خبز, الأرز, قمح, سعر لحم, سعر سمك, سعر بيض, سعر حبوب, سعر المواد الغذائية, حصاد الغذائية,الغذاء محدود, النازحين, حليب الرضع, مساعدات غذائية,المساعدات الغذائية, برنامج الغذاء العالمي, كهرباء, غاز الطبخ, عدم توفر الغذاء, الحصول على الغذاء, مجاعة, حصار, انعدام الأمن الغذائي, الاقتراض الطعام, استهلاك الطعام, الاحتياجات الغذائية, إغلاق السوق,احتكار المواد الغذائية, الزراعة,لا يوجد طعام \n\nالصراع ,اليمن,الغذاء اليمن ,الماء اليمن, الكهرباء اليمن, الغاز اليمن, الزراعة اليمن, المحاصيل اليمن, الحصاد اليمن, الخبز اليمن الحليب",

      "geo": "41.82,12.11,54.53,19.0",

      "status": "NOT_RUNNING",

      "labelCount": 11,

      "humanTaggedCount": null

   },

   {

      "code": "151008190710_digitalindia",

      "name": "DigitalIndia",

      "totalCount": 4605,

      "language": "en,ar",

      "curator": "latika1912",

      "collectionCreationDate": 1444300662000,

      "endDate": 1444301648000,

      "startDate": 1446574572000,

      "keywords": "#a, #an",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Westgate2013_v2",

      "name": "Westgate 2013 v2",

      "totalCount": 199665,

      "language": "",

      "curator": "CrisisMappers",

      "collectionCreationDate": 1379863524000,

      "endDate": null,

      "startDate": 1379940504000,

      "keywords": "#westgate, #weareone",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Test09183",

      "name": "Test",

      "totalCount": 0,

      "language": "en, nl",

      "curator": "EvertVdBr",

      "collectionCreationDate": 1379941274000,

      "endDate": null,

      "startDate": 1379941312000,

      "keywords": "#iOS7 #apple #iphone #ipad",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Pakistan_EQ2013",

      "name": "Earthquake Pakistan Sep-2013",

      "totalCount": 101828,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1380104637000,

      "endDate": 1382272298000,

      "startDate": 1382272229000,

      "keywords": "#Pakistan, #Awaran, #Balochistan, #earthquake, #ReliefPK, #Turbat, #Quetta, #Khuzdar, #Khārān, #karachi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "testMIT",

      "name": "Hurrincan",

      "totalCount": 0,

      "language": "",

      "curator": "micmickimo",

      "collectionCreationDate": 1381415026000,

      "endDate": null,

      "startDate": 1381415041000,

      "keywords": "#new york",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "CyclonePhailin",

      "name": "Cyclone Phailin",

      "totalCount": 1535,

      "language": null,

      "curator": "PatrickMeier",

      "collectionCreationDate": 1381508199000,

      "endDate": 1383852186000,

      "startDate": 1381508214000,

      "keywords": "#Phailin",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "aus_oct2013_geo",

      "name": "Australia Fire - 2013 - geo",

      "totalCount": 147535,

      "language": "en",

      "curator": "OlteanuSandra",

      "collectionCreationDate": 1382526676000,

      "endDate": 1383152277000,

      "startDate": 1382526692000,

      "keywords": "",

      "geo": "137.31,-43.64,153.64,-22.52",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "sudan_re_oct-13",

      "name": "Sudan Revolts",

      "totalCount": 15,

      "language": "en",

      "curator": "helenapuigl",

      "collectionCreationDate": 1382541855000,

      "endDate": null,

      "startDate": 1382543851000,

      "keywords": "#sudanrevolts, #abena",

      "geo": "5.9559,45.818,10.4921,47.8084",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "sudan__0_oct-13",

      "name": "Sudan Revolts (no geo)",

      "totalCount": 5,

      "language": "",

      "curator": "helenapuigl",

      "collectionCreationDate": 1382542024000,

      "endDate": 1382543843000,

      "startDate": 1382542386000,

      "keywords": "#sudanrevolts, #abena",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "westgate_oct-13",

      "name": "Westgate",

      "totalCount": 5,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1382621698000,

      "endDate": 1382622655000,

      "startDate": 1382621714000,

      "keywords": "#westgate ",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_oct-13",

      "name": "Empowering women",

      "totalCount": 0,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1382621880000,

      "endDate": 1382622727000,

      "startDate": 1382622715000,

      "keywords": "#empoweringwomen  #women",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "oil_corr_oct-13",

      "name": "Oil corruption Nigeria",

      "totalCount": 0,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1382622897000,

      "endDate": 1383513202000,

      "startDate": 1382623289000,

      "keywords": "#oil #oilcorruption #oilnigeria #corruptionnigeria  #oiltheft  #nigeria",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "nsw_bush_oct-13",

      "name": "NSW Bushfires",

      "totalCount": 0,

      "language": "en",

      "curator": "KeeraP",

      "collectionCreationDate": 1383063139000,

      "endDate": 1383063769000,

      "startDate": 1383063153000,

      "keywords": "#NSWfires, #QFRS, ",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "environm_oct-13",

      "name": "Social justice & human rights",

      "totalCount": 190,

      "language": "en",

      "curator": "KeeraP",

      "collectionCreationDate": 1383063754000,

      "endDate": 1382994000000,

      "startDate": 1383063831000,

      "keywords": "#humanrights, #poverty, #hunger, #aid, #humantrafficking",

      "geo": "35.7166,32.3111,42.3763,37.3206",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "floods_oct-13",

      "name": "floods",

      "totalCount": 0,

      "language": "en",

      "curator": "GlobalFloodNews",

      "collectionCreationDate": 1383120729000,

      "endDate": 1383120828000,

      "startDate": 1383120741000,

      "keywords": "#flood,#flooding",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "tropical_nov-13",

      "name": "Tropical Cyclone THIRTY-13 ",

      "totalCount": 70980,

      "language": "en, vi, km,",

      "curator": "Fidget02",

      "collectionCreationDate": 1383515530000,

      "endDate": 1383853129000,

      "startDate": 1383853089000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat,\n#KampongCham, #KampongChhnang, #Kandal,\t#KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh\t\n#TayNinh,\t#MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong,\t#NinhThuan,\t#PhuYen,\t#SongBe, #BaRia-VungTau,\t #DongThap #HoChiMinh, #KienGiang,\t#LongAn,\t#TienGiang, #BenTre, #CanTho, \t#Soctrang,\t#TraVinh, #VinhLong,\t#Philippines, #SouthernTagalog,\t#palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiua",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "typhoon__nov-13",

      "name": "Typhoon Yolanda 2013",

      "totalCount": 97675,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1383852686000,

      "endDate": 1383880842000,

      "startDate": 1383852708000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal,\t#KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh\t #TayNinh,\t#MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong,\t#NinhThuan,\t#PhuYen,\t#SongBe, #BaRia-VungTau,\t #DongThap #HoChiMinh, #KienGiang,\t#LongAn,\t#TienGiang, #BenTre, #CanTho, #Soctrang,\t#TraVinh, #VinhLong,\t#Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan",

      "geo": "117.0,6.57,127.07,21.53",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "yolanda_nov-13",

      "name": "Yolanda",

      "totalCount": 151875,

      "language": "en, vi, km,",

      "curator": "Fidget02",

      "collectionCreationDate": 1383853207000,

      "endDate": 1397843349000,

      "startDate": 1397828408000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal, #KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh #TayNinh, #MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong, #NinhThuan, #PhuYen, #SongBe, #BaRia-VungTau, #DongThap #HoChiMinh, #KienGiang, #LongAn, #TienGiang, #BenTre, #CanTho, #Soctrang, #TraVinh, #VinhLong, #Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan, ",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Yolanda_Tagalog",

      "name": "Typhoon Yolanda 2013 Tagalog",

      "totalCount": 0,

      "language": "tl",

      "curator": "CrisisMappers",

      "collectionCreationDate": 1383856373000,

      "endDate": null,

      "startDate": 1383917991000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal,\t#KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh\t #TayNinh,\t#MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong,\t#NinhThuan,\t#PhuYen,\t#SongBe, #BaRia-VungTau,\t #DongThap #HoChiMinh, #KienGiang,\t#LongAn,\t#TienGiang, #BenTre, #CanTho, #Soctrang,\t#TraVinh, #VinhLong,\t#Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan",

      "geo": "117.0,6.57,127.07,21.53",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "TyphoonYolanda2",

      "name": "Typhoon Yolanda 2",

      "totalCount": 82785,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1383880830000,

      "endDate": 1383906154000,

      "startDate": 1383880853000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal,\t#KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh\t #TayNinh,\t#MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong,\t#NinhThuan,\t#PhuYen,\t#SongBe, #BaRia-VungTau,\t #DongThap #HoChiMinh, #KienGiang,\t#LongAn,\t#TienGiang, #BenTre, #CanTho, #Soctrang,\t#TraVinh, #VinhLong,\t#Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan",

      "geo": "117.0,6.57,127.07,21.53",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "TyphoonYolanda3",

      "name": "Typhoon Yolanda 3",

      "totalCount": 4805,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1383906140000,

      "endDate": null,

      "startDate": 1383956233000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal,\t#KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh\t #TayNinh,\t#MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong,\t#NinhThuan,\t#PhuYen,\t#SongBe, #BaRia-VungTau,\t #DongThap #HoChiMinh, #KienGiang,\t#LongAn,\t#TienGiang, #BenTre, #CanTho, #Soctrang,\t#TraVinh, #VinhLong,\t#Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan",

      "geo": "117.0,6.57,127.07,21.53",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "yolanda__nov-13",

      "name": "Yolanda 2",

      "totalCount": 450,

      "language": "English (en) ",

      "curator": "Fidget02",

      "collectionCreationDate": 1383906157000,

      "endDate": 1391954564000,

      "startDate": 1391954547000,

      "keywords": "\n\n#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal, #KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh #TayNinh, #MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong, #NinhThuan, #PhuYen, #SongBe, #BaRia-VungTau, #DongThap #HoChiMinh, #KienGiang, #LongAn, #TienGiang, #BenTre, #CanTho, #Soctrang, #TraVinh, #VinhLong, #Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "yoland_0_nov-13",

      "name": "Yolanda Set3",

      "totalCount": 73130,

      "language": "en, vi, km,",

      "curator": "Fidget02",

      "collectionCreationDate": 1383938481000,

      "endDate": null,

      "startDate": 1383998311000,

      "keywords": "#PhanRang, #Cyclonethirty, #Vietnam, #Philippines, #Cambodia, #KaohKong, #Pouthisat, #KampongCham, #KampongChhnang, #Kandal, #KampongSpoe, #KampongThum, #PhnumPenh, #PreyVeng, #Kracheh #TayNinh, #MondolKiri, #VietNam, #DacLac, #KhanhHoa, #LamDong, #NinhThuan, #PhuYen, #SongBe, #BaRia-VungTau, #DongThap #HoChiMinh, #KienGiang, #LongAn, #TienGiang, #BenTre, #CanTho, #Soctrang, #TraVinh, #VinhLong, #Philippines, #SouthernTagalog, #palau, #HoChiMinhCity, #PhnumPénh, #ThànhPhoHoChíMinh, #PhanRang, #LongAn, #cyclone, #tropicalcyclone, #thirty13, #PhanRang, #ThonTriThuy, #ApBinhBa, #TuyPhong, #ApVinhHao, #HoaDa, #ApHoiTam, #ThonLacNghie, #ThonSonHai, #ApVinhTrieu, #Roxas, #SanNicholas, #Rizal, #Malcampo, #stormsurge, #typhoon, #haiyan, #Palani, #yolanda, #typhoonyolanda, #QuangNam-DaNang, #ThuaThienHue, #CentralVisayas, #WesternVisayas, #Bicol, #SouthernTagalog, #EasternVisayas, #Calbayog, #Kalibo, #Roxas, #Babatñgon, #Barugo, #Carigara, #Palo, #Ormoc, #Burauen, #Tacloban, #Tanauan, #Guiuan, ",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_0_nov-13",

      "name": "Kenya On Twitter",

      "totalCount": 0,

      "language": "en",

      "curator": "blackorwa",

      "collectionCreationDate": 1384327283000,

      "endDate": 1384327448000,

      "startDate": 1384327313000,

      "keywords": "#kot, #KOT",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "uk_track_nov-13",

      "name": "UK_tracker",

      "totalCount": 280,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1384799866000,

      "endDate": 1392141558000,

      "startDate": 1392141546000,

      "keywords": "uk",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "iccm_201_nov-13",

      "name": "ICCM 2013",

      "totalCount": 0,

      "language": "en",

      "curator": "CrisisMappers",

      "collectionCreationDate": 1384842685000,

      "endDate": null,

      "startDate": 1384845823000,

      "keywords": "#iccm, iccm, #crisismappers, crisismappers, #ICCM2013, iccm2013",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "crisis_m_nov-13",

      "name": "Crisis Mappers Conf",

      "totalCount": 3520,

      "language": "",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1384846933000,

      "endDate": 1389005261000,

      "startDate": 1389005216000,

      "keywords": "#iccm, iccm, #crisismappers, crisismappers, #ICCM2013, iccm2013",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "poplava_nov-13",

      "name": "Poplava",

      "totalCount": 0,

      "language": "en,mk",

      "curator": "PopovskiVasko",

      "collectionCreationDate": 1384927211000,

      "endDate": 1384927437000,

      "startDate": 1384927230000,

      "keywords": "#poplava",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "e_nov-13",

      "name": "Earthquake",

      "totalCount": 10,

      "language": "en",

      "curator": "PopovskiVasko",

      "collectionCreationDate": 1384927421000,

      "endDate": 1384960312000,

      "startDate": 1384960261000,

      "keywords": "#earthquake, #macedonia ",

      "geo": "0422600,0404600",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "#gcdc201_nov-13",

      "name": "#gcdc2013",

      "totalCount": 0,

      "language": "en",

      "curator": "ishuah_",

      "collectionCreationDate": 1384957577000,

      "endDate": 1402659365000,

      "startDate": 1402659098000,

      "keywords": "#gcdc2013 #googlechallenge #cloudstorage #appengine",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "aleppo_s_nov-13",

      "name": "Aleppo Syria Test",

      "totalCount": 215,

      "language": "en",

      "curator": "mattrmcnabb",

      "collectionCreationDate": 1384957741000,

      "endDate": 1384958023000,

      "startDate": 1384957760000,

      "keywords": "#syria, #aleppo",

      "geo": "37.00567,36.097914,37.399827,36.338061",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "iccm_nov-13",

      "name": "ICCM",

      "totalCount": 0,

      "language": "en",

      "curator": "mattrmcnabb",

      "collectionCreationDate": 1384958007000,

      "endDate": null,

      "startDate": 1384958033000,

      "keywords": "#ICCM, #ICCM2013",

      "geo": "36.620166,-1.443253,37.1338,-1.123253",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "nyumba_k_nov-13",

      "name": "Nyumba Kumi",

      "totalCount": 0,

      "language": "en",

      "curator": "levisdoban",

      "collectionCreationDate": 1385544905000,

      "endDate": 1385545070000,

      "startDate": 1385617116000,

      "keywords": "#nyumbakumi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "ma3route_nov-13",

      "name": "ma3route",

      "totalCount": 0,

      "language": "en",

      "curator": "levisdoban",

      "collectionCreationDate": 1385545052000,

      "endDate": 1385617103000,

      "startDate": 1385551343000,

      "keywords": "ma3route",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "testing__nov-13",

      "name": "Testing Collection",

      "totalCount": 270,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1385577845000,

      "endDate": 1385577936000,

      "startDate": 1385577916000,

      "keywords": "UK",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "indonesia",

      "name": "Indonesia volcano",

      "totalCount": 0,

      "language": "en",

      "curator": "AlexMikhashchuk",

      "collectionCreationDate": 1385643239000,

      "endDate": 1389300780000,

      "startDate": 1389300555000,

      "keywords": "Indonesia volcano, #Indonesia, #volcano, #eruption, volcano eruption, jakarta, Sumatra, Sumatra volcano, Sumatra volcano eruption, Sumatra eruption",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_dec-13",

      "name": "Haiyan typhoon",

      "totalCount": 0,

      "language": "en",

      "curator": "MinhTienNguyen2",

      "collectionCreationDate": 1385991465000,

      "endDate": null,

      "startDate": 1385991481000,

      "keywords": "#haiyan",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "test_res_dec-13",

      "name": "Test restart 20131203",

      "totalCount": 35,

      "language": "en",

      "curator": "ManzanaMecanica",

      "collectionCreationDate": 1386075722000,

      "endDate": 1386075801000,

      "startDate": 1386075777000,

      "keywords": "japan",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "dec_stor_dec-13",

      "name": "Dec Storm 2013",

      "totalCount": 1190,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1386245864000,

      "endDate": 1386258699000,

      "startDate": 1386245881000,

      "keywords": "#stormuk, #ukstorm, #ukflood",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "dec_st_0_dec-13",

      "name": "Dec Storm 20131",

      "totalCount": 905,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1386258751000,

      "endDate": 1386262673000,

      "startDate": 1386258766000,

      "keywords": "#ukstorm, #stormuk",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "dec_st_1_dec-13",

      "name": "dec storm 3",

      "totalCount": 15710,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1386262705000,

      "endDate": 1386326988000,

      "startDate": 1386327462000,

      "keywords": "#ukstorm, #stormuk",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "ukraine",

      "name": "Protests in Ukraine",

      "totalCount": 0,

      "language": "",

      "curator": "ingmarweber",

      "collectionCreationDate": 1387189053000,

      "endDate": null,

      "startDate": 1387439027000,

      "keywords": "#Євромайдан, #ЕвроМайдан, #ЄвроМайдан, #EuroMaidan, #Кличко, #КличкоПоветкин, #майдан, #Україна, #беркут, #київ, #Янукович, #антимайдан",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2013-12-westgate",

      "name": "Westgate_v1",

      "totalCount": 0,

      "language": "en",

      "curator": "KenyaRedCross",

      "collectionCreationDate": 1387452292000,

      "endDate": 1387452381000,

      "startDate": 1387452308000,

      "keywords": "#westgate",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2013-12-itrade4good",

      "name": "iTrade4Good",

      "totalCount": 0,

      "language": "en",

      "curator": "KenyaRedCross",

      "collectionCreationDate": 1387452339000,

      "endDate": null,

      "startDate": 1387452389000,

      "keywords": "#itrade4good",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2013-12-syria",

      "name": "Syria",

      "totalCount": 90,

      "language": "en,ar",

      "curator": "clemonb_r",

      "collectionCreationDate": 1387468531000,

      "endDate": 1387468936000,

      "startDate": 1387468547000,

      "keywords": "#syria",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_1013-12-",

      "name": "Skopje",

      "totalCount": 0,

      "language": "en",

      "curator": "IgorMiskovski",

      "collectionCreationDate": 1388158142000,

      "endDate": 1394057909000,

      "startDate": 1394057892000,

      "keywords": "#pornovtornik",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-01-us_freeze",

      "name": "US freeze",

      "totalCount": 268230,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1389112589000,

      "endDate": 1389726275000,

      "startDate": 1389201964000,

      "keywords": "#Chiberia, #polarvortex, #globalwarming, #frozensharknado, #California, #Michigan, #Chicago, #Frostbite, #blizzard2014",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-01-umati_january_2",

      "name": "UMATI JANUARY 2",

      "totalCount": 1551465,

      "language": "en,ki,sw",

      "curator": "faithumati",

      "collectionCreationDate": 1389339763000,

      "endDate": 1396534382000,

      "startDate": 1396523425000,

      "keywords": "kihii, jarabuon, votingrobots, madoadoa, muslims, terrorists, mt kenya mafia, okuyu, 41against1, shetani, mademini, vipii, kipii, vihii, jiggers, mpigs, mashetani, wasepere, wajaka, longsleeve, #kenyaat50, #keat50, #sickat50, #mututho, mututho, kenya@50, kenya, #kenya, #westgate, NYPD, #NYPD, uhuru, jubilee, uhunye, #KOT, #mykenya, nairobi, mombasa, kisumu, sick @ 50, sick@50, kikuyu's, kikuyu, luo, kalenjins, luos, kalenjin, nandi, somali, somalis, wasomali, walalo, manugu, maumbwa, mbwa, umbwa, maumau, #MauMau, kenyans, okuyu, waislamu, masjid, #masjidmusa, #masjidtawfiq, #masjidmusamosque, ATPU,  kikuyus are thieves, #dupedat50, kenyatta, #kenyatta, #weareone, #weareone_Dering, harambee, #harambee, kenya at filthy, wajaka, #WeAreNotOne, #kenyaatfilthy, Pnagani, grenadeattack, Wajir, Garissa, Moyale, Nyumba Kumi, #NyumbaKumi, Ole Lenku, evil society, Kimaiyo, Kimayo, somali's, kamwana, UoN, #ICCTrials, #ICCTRilasKE, wamalize, wamalizwe, wauwawe, wauwe, waue, fagia, wafagie, waende kw",

      "geo": "33.91,-4.68,41.91,5.03",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-modena",

      "name": "Modena",

      "totalCount": 0,

      "language": "en,it",

      "curator": "CIMASocial",

      "collectionCreationDate": 1391519509000,

      "endDate": 1391520911000,

      "startDate": 1391519706000,

      "keywords": "#allertameteoER",

      "geo": "44, 45, 10, 12",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-hashtags_and_keywords",

      "name": "Hashtags and Keywords",

      "totalCount": 756560,

      "language": "en,sw",

      "curator": "NiNanjira",

      "collectionCreationDate": 1391589059000,

      "endDate": 1397142001000,

      "startDate": 1396966321000,

      "keywords": "#Likoni, Likoni, #homosexuality #homosexuals, #gayism, SUPKEM, #SUPKEM, Mombasa, #Mombasa, Nairobi, #Nairobi, Masjid Musa, #MasjidMusa, MasjidMosque, #Masjid,  Uhuru, #Uhuru, Majengo, #Majengo, Kisauni, #Kisauni, Ruto, Waki, #ICCDebate, Al Shabab, Al Shabaab, AlKebab, Al Kebab, Alshabab, Alshabaab, #Feb13Protest, #DiaperMentality, #LikoniAttack, Kimaiyo, Kimayo, Ole Lenku, Lenku, Somalis,Muslims, PoliceKE, IGKimaiyo, joelenku, ndockenya, harryharrey1, #AICChurchLikoni, #ChurchAttack, Kenyan Muslims, #FoulWinds, #ATPU, ATPU, Makaburi, Aboud Rogo, Sheikh Rogo, makafiri, kafiri, kafir, Fazul, Ilunga Hassan Kapungu, Duale, Aden Duale, #MombasaShooting, Marwa, Kilifi, Malindi, Lamu, Eastleigh, #EastleighBlast, Makaburi, #Makaburi, Rogo, Aboud Rogo, #rogo, shanzu, Bomas, Mahat Omar, Juja, Muslim clerics, muslim cleric, Kenyan Somalis, Muhuri, Dadaab, Garissa, Garrisa, Kongowea, #KasaraniConcentrationCamp",

      "geo": "36.645419,-1.444863,37.049375,-1.164744, 39.569149,-4.118713,39.763029,-3.952497, 40.896757,-2.280112,40.9052,-2.262424,40.024738,-3.30745,40.144152,-3.151141, 39.833593,-3.679947,39.886293,-3.588464, 39.647079,-4.099795,39.674774,-4.079346",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-umati_revised_keywordsUmati Revised Keywords",

      "name": "Umati Revised Keywords",

      "totalCount": 46560,

      "language": "en,sw",

      "curator": "blackorwa",

      "collectionCreationDate": 1391769898000,

      "endDate": 1396098016000,

      "startDate": 1395924702000,

      "keywords": "hatespeech,luos,jaluo,okuyu,foreskin,msapere,jarabuon,a good kikuyu,wakikuyu,wakikuyu wote,we will circumcise,wajaluo ni ma,kuchapwa na bibi,wezi wa kura,uncircumcised luo,uncircumcised jaluo,mkikuyu mzuri,wajinga,mtahiri,kafiri,kihii,kihee,nyeri men",

      "geo": "33.8,-4.9,42.0,5.6",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-uk_flood_geo",

      "name": "UK Flood geo",

      "totalCount": 1452195,

      "language": "en",

      "curator": "sbucur_dslab",

      "collectionCreationDate": 1392144508000,

      "endDate": 1392798832000,

      "startDate": 1392232798000,

      "keywords": "",

      "geo": "-2.28,50.8,1.35,52.0",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-",

      "name": "Terremoto 2014",

      "totalCount": 0,

      "language": "it",

      "curator": "danife75",

      "collectionCreationDate": 1392194483000,

      "endDate": 1396818000000,

      "startDate": 1396861264000,

      "keywords": "#terremoto",

      "geo": "5.3407,43.4915,9.8768,45.4819",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-java_volcano_2014",

      "name": "Java Volcano 2014",

      "totalCount": 22175,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1392386290000,

      "endDate": 1392798787000,

      "startDate": 1392386311000,

      "keywords": "#volcano, #indonesia, #Kelud",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-japan_snow_storm",

      "name": "Japan Snow Storm",

      "totalCount": 26800,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1392629626000,

      "endDate": 1392803486000,

      "startDate": 1392802438000,

      "keywords": "japan snow, snowstorm, #japan, japansnow",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-georgia_snowstorm",

      "name": "Georgia snowstorm",

      "totalCount": 5,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1392655997000,

      "endDate": 1392656136000,

      "startDate": 1392656014000,

      "keywords": "#gasnow, #snowpocalypse",

      "geo": "-84.551819,33.647808,-84.289108,33.887618",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-cold_and_flu_trends",

      "name": "Cold and Flu Trends",

      "totalCount": 586415,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1392822394000,

      "endDate": 1393006215000,

      "startDate": 1392993409000,

      "keywords": "tamiflu, benadryl,  robitussin, advil, kleenix, kleenex, nyquil, dayquil, cough drops, bedridden, ear ache, flu, headache, muscle aches, nausea, puke, sore throat, stomach ache, strep, stuffy, throat hurts, everything hurts, feel crappy, feel shitty, threw up, i am sick, i'm sick, everyone is sick, everyone's sick, i am sneezing, i'm sneezing",

      "geo": "-125.54,24.35,-66.55,49.38",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_0014-02-",

      "name": "Kiev Clashes Feb 2014",

      "totalCount": 0,

      "language": "en,it,",

      "curator": "danife75",

      "collectionCreationDate": 1392909451000,

      "endDate": 1392930000000,

      "startDate": 1392909473000,

      "keywords": "#kiev",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-02-prova",

      "name": "prova",

      "totalCount": 0,

      "language": "it",

      "curator": "cosimoversace",

      "collectionCreationDate": 1392977998000,

      "endDate": 1392978163000,

      "startDate": 1392978017000,

      "keywords": "#ferreggiano",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "genova",

      "name": "prova3",

      "totalCount": 0,

      "language": "it",

      "curator": "cosimoversace",

      "collectionCreationDate": 1392978819000,

      "endDate": null,

      "startDate": 1392978838000,

      "keywords": "#genova",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "code1",

      "name": "Test za xFactor",

      "totalCount": 0,

      "language": "Any language",

      "curator": "lastdevelop",

      "collectionCreationDate": 1393366594000,

      "endDate": 1393368083000,

      "startDate": 1393367435000,

      "keywords": "хфактор",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014_02-prova",

      "name": "prova_v1",

      "totalCount": 1030,

      "language": "it",

      "curator": "alessandrobura",

      "collectionCreationDate": 1393503751000,

      "endDate": 1393576902000,

      "startDate": 1393503971000,

      "keywords": "genova",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ukraine",

      "name": "Ukraine/Crimea",

      "totalCount": 11335,

      "language": "en",

      "curator": "Fidget02",

      "collectionCreationDate": 1393699865000,

      "endDate": 1393700854000,

      "startDate": 1393700809000,

      "keywords": "Ukraine, Russia, Crimea, Balaclava, Ukrainian, Putin, extremists,  extremist groups, Ukrainians, Kharkiv, Odessa, Ukrainprotests, Donetsk, Freedom, Simferopol, Viktor Yanukovych,",

      "geo": "22.14,44.39,53.32,57.82",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-yolsuzluk",

      "name": "Yolsuzluk",

      "totalCount": 1375,

      "language": "en,tr",

      "curator": "atsengul",

      "collectionCreationDate": 1393916245000,

      "endDate": 1394538684000,

      "startDate": 1393916355000,

      "keywords": "#direngezi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-",

      "name": "Fire",

      "totalCount": 1185230,

      "language": "en",

      "curator": "IgorMiskovski",

      "collectionCreationDate": 1394057886000,

      "endDate": 1394538653000,

      "startDate": 1394057915000,

      "keywords": "fire",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-cold_and_flu_trends_2",

      "name": "Cold and Flu Trends 2",

      "totalCount": 765,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1394212178000,

      "endDate": 1394213312000,

      "startDate": 1394213212000,

      "keywords": "tamiflu, benadryl, robitussin, advil, kleenix, kleenex, nyquil, dayquil, \"cough drops\", bedridden, \"ear ache\", flu, headache, \"muscle aches\", nausea, puke, \"sore throat\", \"stomach ache\", strep, stuffy, \"throat hurts\", \"everything hurts\", \"feel crappy\", \"feel shitty\", \"threw up\", \"i am sick\", \"i'm sick\", \"everyone is sick\", \"everyone's sick\", \"i am sneezing\", \"i'm sneezing\"",

      "geo": "-125.3,25.0,-49.9,50.5",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-cold_medicines",

      "name": "Cold Medicines",

      "totalCount": 315,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1394213952000,

      "endDate": 1394214895000,

      "startDate": 1394214296000,

      "keywords": "tamiflu, benadryl, robitussin, advil, kleenix, kleenex, nyquil, dayquil, \"feel sick\"",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-feel_sick",

      "name": "Feel Sick",

      "totalCount": 470,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1394214918000,

      "endDate": 1394475521000,

      "startDate": 1394474774000,

      "keywords": "feel sick, wayne rooney, duck dynasty",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-i'm_sick",

      "name": "I'm sick",

      "totalCount": 0,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1394215102000,

      "endDate": 1394219561000,

      "startDate": 1394216331000,

      "keywords": "\"i'm sick\"",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-pk-vs-sirlka",

      "name": "PK-Vs-SIRLKA",

      "totalCount": 63290,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1394262338000,

      "endDate": 1394299641000,

      "startDate": 1394282592000,

      "keywords": "#AsiaCup, #cricket, #PakvSL, #AsiaCupFinal",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-test_cases_1",

      "name": "Test Cases 1",

      "totalCount": 155,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1394475670000,

      "endDate": 1394475974000,

      "startDate": 1394475774000,

      "keywords": "i'm sick, rock and roll, my mom is",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-cold_and_flu_trends_3",

      "name": "Cold and Flu Trends 3",

      "totalCount": 157645,

      "language": "en",

      "curator": "tymoriel",

      "collectionCreationDate": 1394476185000,

      "endDate": 1394538604000,

      "startDate": 1394476257000,

      "keywords": "tamiflu, benadryl, robitussin, advil, kleenix, kleenex, nyquil, dayquil, cough drops, bedridden, ear ache, flu, headache, muscle aches, nausea, puke, sore throat, stomach ache, strep, stuffy, throat hurts, everything hurts, feel crappy, feel shitty, threw up, i am sick, i'm sick, everyone is sick, everyone's sick, i am sneezing, i'm sneezing",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "NairobiTest",

      "name": "Nairobi",

      "totalCount": 5,

      "language": "en",

      "curator": "blackorwa",

      "collectionCreationDate": 1394536508000,

      "endDate": 1394536772000,

      "startDate": 1394536527000,

      "keywords": "nairobi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-manhattan",

      "name": "Manhattan Explosion",

      "totalCount": 343375,

      "language": "en",

      "curator": "blackorwa",

      "collectionCreationDate": 1394633017000,

      "endDate": 1394704970000,

      "startDate": 1394633967000,

      "keywords": "Manhattan,park ave,new york,park avenue,nypd, east harlem",

      "geo": "-74.047285,40.679548,-73.907,40.882214",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-harlem_explosion",

      "name": "harlem explosion",

      "totalCount": 367335,

      "language": "en",

      "curator": "velofemme",

      "collectionCreationDate": 1394633984000,

      "endDate": 1395308843000,

      "startDate": 1394645375000,

      "keywords": "#harlem, harlem, explosion, east harlem",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_1014-03-",

      "name": "Big Data",

      "totalCount": 5,

      "language": "en",

      "curator": "kasshout",

      "collectionCreationDate": 1394944246000,

      "endDate": 1418187611000,

      "startDate": 1418013250000,

      "keywords": "#bigdatamed",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-syriachildren",

      "name": "Children of Syria",

      "totalCount": 123505,

      "language": "en",

      "curator": "kasshout",

      "collectionCreationDate": 1394944999000,

      "endDate": 1395308829000,

      "startDate": 1394945014000,

      "keywords": "#childrenofsyria",

      "geo": "35.7166,32.3111,42.3763,37.3206",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "WagebillKenya_2014",

      "name": "Wagebill Debate",

      "totalCount": 8855,

      "language": "en",

      "curator": "wandabwaherman",

      "collectionCreationDate": 1394953806000,

      "endDate": 1395308820000,

      "startDate": 1394953844000,

      "keywords": "#wagebillKE,#Salaries,#Serem,#Uhuru,#Salarycut",

      "geo": "36.645419,-1.444863,37.049375,-1.164744",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-t20-BANvAFG",

      "name": "T20-BANvAFG",

      "totalCount": 28290,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1394963056000,

      "endDate": 1395000323000,

      "startDate": 1394984680000,

      "keywords": "BANvAFG, T20, #WT20, #AfgvBan, NepvsHk",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-kalavritaklv",

      "name": "kalavrita",

      "totalCount": 0,

      "language": "en",

      "curator": "AlexGzs",

      "collectionCreationDate": 1395212419000,

      "endDate": 1395212554000,

      "startDate": 1395212438000,

      "keywords": "#xkk #kalavrita",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-T20_IndvPk",

      "name": "T20-IndvPak‬",

      "totalCount": 86510,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1395389901000,

      "endDate": 1395420945000,

      "startDate": 1395399672000,

      "keywords": "IndvPak‬, cricket, #wt20, #PakvInd",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-T20_PAKvAUS",

      "name": "T20_PAKvAUS",

      "totalCount": 116105,

      "language": "",

      "curator": "empirical7",

      "collectionCreationDate": 1395558684000,

      "endDate": 1395580545000,

      "startDate": 1395565991000,

      "keywords": "PAKvAUS, AUSvPAK, Cricket, T20, WT20",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-turkey-twitter_shutdown",

      "name": "Turkey-Twitter shutdown",

      "totalCount": 213735,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1395560623000,

      "endDate": 1395734401000,

      "startDate": 1395560732000,

      "keywords": "Turkey, #TwitterisblockedinTurkey",

      "geo": "26.25,37.16,42.91,41.9",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-t20_indvswesti",

      "name": "T20_INDvsWestI",

      "totalCount": 84615,

      "language": "",

      "curator": "empirical7",

      "collectionCreationDate": 1395580410000,

      "endDate": 1395754858000,

      "startDate": 1395754686000,

      "keywords": "IndvsWI, WivsInd, cricket, Wt20, T20",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-tribes_and_politicians",

      "name": "Tribes, Politicians and Places",

      "totalCount": 25695,

      "language": "en",

      "curator": "iHubResearch",

      "collectionCreationDate": 1395645590000,

      "endDate": 1396504806000,

      "startDate": 1396374800000,

      "keywords": "Kikuyu, Kikuyus, Luo, Luos, Kalenjin, Kalenjins, Kamba, Kambas, Kisii, Kisiis, Luhya, Luhyas, Somali, Somalis, Meru, Merus, Embu, Embus, Kuria, Kurias, Coasterians, Jang'os, Jang'o, Kyuks, sapere, wasapere, msapere, mjaka, wajaka, walalo, walalos",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-likoni_attack",

      "name": "Likoni Attack",

      "totalCount": 9675,

      "language": "en",

      "curator": "blackorwa",

      "collectionCreationDate": 1395678924000,

      "endDate": 1395924696000,

      "startDate": 1395678939000,

      "keywords": "#LikoniAttack",

      "geo": "33.91,-4.81,41.9,4.62",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-healthy_living",

      "name": "Healthy Living",

      "totalCount": 686720,

      "language": "en,",

      "curator": "ingmarweber",

      "collectionCreationDate": 1395727231000,

      "endDate": 1395903602000,

      "startDate": 1395727292000,

      "keywords": "healthy, health",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-(test)_demonstrators_antwerp_belgium",

      "name": "(Test) Demonstrators Antwerp Belgium",

      "totalCount": 6310,

      "language": "en,nl,fr",

      "curator": "pminfobe",

      "collectionCreationDate": 1395832189000,

      "endDate": 1395943219000,

      "startDate": 1395854058000,

      "keywords": "foorkramers,foorkramer,Sinksenfoor,Sinksenfoorprotest,#antwerpen,file antwerpen,blokkade antwerpen,protest antwerpen,foorapen,Singel,foorwijf,fooraap,foorapen",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-test-qwe",

      "name": "test-qwe",

      "totalCount": 0,

      "language": "en",

      "curator": "AlexMikhashchuk",

      "collectionCreationDate": 1395937033000,

      "endDate": 1395937167000,

      "startDate": 1395937076000,

      "keywords": "#test",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-ucraine",

      "name": "Ucraine",

      "totalCount": 15,

      "language": "en",

      "curator": "liberalogica",

      "collectionCreationDate": 1396028810000,

      "endDate": 1396028913000,

      "startDate": 1396028824000,

      "keywords": "#ukraine",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-t20-pakvsbang",

      "name": "T20-PAKvsBANG",

      "totalCount": 34630,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1396171826000,

      "endDate": 1396185090000,

      "startDate": 1396184145000,

      "keywords": "cricket, PakvBan, BanvPak, WT20",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-t20-indvaus",

      "name": "T20-IndvAus",

      "totalCount": 38665,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1396184090000,

      "endDate": 1396201670000,

      "startDate": 1396185104000,

      "keywords": "IndvAus, AusvInd, WT20, cricket",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-cyclone_helen",

      "name": "Cyclone Hellen",

      "totalCount": 160,

      "language": "en,fr,pt",

      "curator": "Fidget02",

      "collectionCreationDate": 1396251070000,

      "endDate": 1396252046000,

      "startDate": 1396252052000,

      "keywords": "cyclone, hellen, cyclone hellen, Madagascar, Boanamary, Mahajanga, Besalampy, Mahajanga, Amborovy, Soalala, Majunga, Soalala, bali, Melaky, Boeny, Bay of Baly,",

      "geo": "41.78,-25.61,50.48,-11.95",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-03-race_swap",

      "name": "Race swap",

      "totalCount": 155,

      "language": "en",

      "curator": "velofemme",

      "collectionCreationDate": 1396278737000,

      "endDate": 1396735226000,

      "startDate": 1396559716000,

      "keywords": "#raceswapexp, #RaceSwapExp, raceswapexp",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-t20-pakvwi",

      "name": "T20-PakvWI",

      "totalCount": 75225,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1396356374000,

      "endDate": 1396375198000,

      "startDate": 1396374786000,

      "keywords": "cricket, PAKvWI, wt20, PAKvsWI, WIvsPAK, WIvPAK",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Haiyan",

      "name": "Haiyan",

      "totalCount": 55,

      "language": "en",

      "curator": "abdulrshahid",

      "collectionCreationDate": 1396520461000,

      "endDate": 1396534418000,

      "startDate": 1396521156000,

      "keywords": "#haiyan, #yolandaph, #tacloban, #reliefph, #pnoy, #rescueph, #aquino, #typhoonaid, #typhoonph, #tracingph",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "test123",

      "name": "test123",

      "totalCount": 5,

      "language": "en",

      "curator": "abdulrshahid",

      "collectionCreationDate": 1396520843000,

      "endDate": 1396521096000,

      "startDate": 1396520869000,

      "keywords": "#bbc",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-tornado_us_april14",

      "name": "Tornado US April 2014",

      "totalCount": 140570,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1396523260000,

      "endDate": 1396735250000,

      "startDate": 1396559502000,

      "keywords": "#tornadoes, tornado",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-prepsummit",

      "name": "PrepSummit",

      "totalCount": 66980,

      "language": "en",

      "curator": "tamer_hadi",

      "collectionCreationDate": 1396561811000,

      "endDate": 1396657857000,

      "startDate": 1396561842000,

      "keywords": "#ps14",

      "geo": "-84.551819,33.647808,-84.289108,33.887618",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-chile_8.2_earthquake_2014",

      "name": "Chile 8.2 Earthquake 2014",

      "totalCount": 325,

      "language": "en",

      "curator": "jaakkoh",

      "collectionCreationDate": 1396604092000,

      "endDate": 1396652757000,

      "startDate": 1396604112000,

      "keywords": "#ChileEarthquake",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-eu_parliament_elections_discussion_monitoring",

      "name": "EU Parliament elections discussion monitoring",

      "totalCount": 1035,

      "language": "en,sv,fi",

      "curator": "jaakkoh",

      "collectionCreationDate": 1396652639000,

      "endDate": 1397191676000,

      "startDate": 1396966273000,

      "keywords": "#EUvaalit, #EPvaalit, #eurovaalit, #vaalit2014",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-opisrael",

      "name": "OpIsrael",

      "totalCount": 15460,

      "language": "en,",

      "curator": "TomerSimon",

      "collectionCreationDate": 1396861667000,

      "endDate": 1397098807000,

      "startDate": 1396966304000,

      "keywords": "#opisrael, #opisraelbirthday, #opisrahell, #hacked, #tangodown",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-www2014",

      "name": "WWW2014",

      "totalCount": 2370,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1396922293000,

      "endDate": 1397191040000,

      "startDate": 1397002123000,

      "keywords": "www2014",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-pennsylvania_school_stabbing",

      "name": "Pennsylvania_school_stabbing",

      "totalCount": 740865,

      "language": "en",

      "curator": "o_saja",

      "collectionCreationDate": 1397057950000,

      "endDate": 1397335377000,

      "startDate": 1397118074000,

      "keywords": "Pennsylvania school, school stabbing, #Pennsylvania, #PA, #pennsylvaniayourinourthoughts, stabbings Pennsylvania, Pennsylvania school, PA school, #stabbing, Franklin school,school stabbings, Pittsburgh school, Franklin stabbings, Pittsburgh stabbing, Pittsburgh stabbings, students stabbed, Murrysville, Franklin Regional School, mass stabbing Pennsylvania, mass stabbing PA, mass stabbing Pittsburgh, Alex Hribal",

      "geo": "-80.52,39.72,-74.69,42.27",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-cyclone_australia_2014",

      "name": "Cyclone Australia 2014",

      "totalCount": 45420,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1397191191000,

      "endDate": 1397395067000,

      "startDate": 1397397638000,

      "keywords": "cyclone, CycloneITA, TCITA, ITA",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-ita_cyclone_australia",

      "name": "ita_cyclone_australia",

      "totalCount": 39140,

      "language": "en",

      "curator": "sbucur",

      "collectionCreationDate": 1397211955000,

      "endDate": 1397395039000,

      "startDate": 1397397579000,

      "keywords": "#cycloneIta, #TCIta, cyclone Ita, #Ita, Cape Flattery, #cooktown, storm ita, #TropicalCyclone, #Cyclone, #STCIta, #Itapocalypse, #cycloneitaportdouglas, #FNQ, #Queensland, australia cyclone, #CapeFlattery, #LochartRiver,#portdouglas, #NorthQLD",

      "geo": "137.99,-29.18,153.6,-9.93",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-cyclone_ita",

      "name": "Cyclone Ita",

      "totalCount": 0,

      "language": "en",

      "curator": "helen_gibson",

      "collectionCreationDate": 1397217247000,

      "endDate": 1397395051000,

      "startDate": 1397397620000,

      "keywords": "#tcita \"cyclone ita\"",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-solomon_earthquake_2014",

      "name": "Solomon Earthquake 2014",

      "totalCount": 1135,

      "language": "en",

      "curator": "PatrickMeier",

      "collectionCreationDate": 1397334490000,

      "endDate": 1397395029000,

      "startDate": 1397397560000,

      "keywords": "#solomon, #solomonearthquake, #solomonquake, #solomontsunami, #solomonislands, #Vanuatu #Nauru #NewCaledonia #Tuvalu #Kosrae",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-04-fires_valparaiso-chile",

      "name": "Fires Valparaiso-Chile",

      "totalCount": 8345,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1397456369000,

      "endDate": 1398243604000,

      "startDate": 1397981932000,

      "keywords": "fire valparaiso, firevalparaiso, chile fire, chilefire, valparaiso fire, valparaisofire, valparaiso wildfires, valparaiso wildfire",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "_0014-04-",

      "name": "Zurich",

      "totalCount": 0,

      "language": "en",

      "curator": "johngouf",

      "collectionCreationDate": 1398695448000,

      "endDate": 1398695491000,

      "startDate": 1398695463000,

      "keywords": "",

      "geo": "7.8456,46.9513,9.2561,47.564",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "dying",

      "name": "Death",

      "totalCount": 5,

      "language": "en",

      "curator": "ingmarweber",

      "collectionCreationDate": 1399809058000,

      "endDate": 1399809172000,

      "startDate": 1399809075000,

      "keywords": "rip, r.i.p.",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-AlecBaldwin",

      "name": "Alec Baldwin",

      "totalCount": 20,

      "language": "en",

      "curator": "RodzWilliams",

      "collectionCreationDate": 1400062313000,

      "endDate": 1400670015000,

      "startDate": 1400062398000,

      "keywords": "#AlecBaldwin #arrested",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378290-7548784",

      "name": "378290",

      "totalCount": 0,

      "language": "",

      "curator": "emscls2",

      "collectionCreationDate": 1400151790000,

      "endDate": 1400176800000,

      "startDate": 1400151897000,

      "keywords": null,

      "geo": "8.19,120.88,10.79,123.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378259-7317233",

      "name": "378259",

      "totalCount": 0,

      "language": "",

      "curator": "emscls1",

      "collectionCreationDate": 1400152021000,

      "endDate": 1400176801000,

      "startDate": 1400152498000,

      "keywords": null,

      "geo": "5.07,143.57,7.67,146.17",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378312-353047",

      "name": "378312",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1400158985000,

      "endDate": 1400184000000,

      "startDate": 1400159053000,

      "keywords": null,

      "geo": "15.93,106.78,16.75,107.60",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3783171852190",

      "name": "378317",

      "totalCount": 0,

      "language": "",

      "curator": "emscls4",

      "collectionCreationDate": 1400161190000,

      "endDate": 1400184001000,

      "startDate": 1400161517000,

      "keywords": null,

      "geo": "-32.48,-72.10,-31.80,-71.42",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-sandeigo_wildfires",

      "name": "SanDeigo Wildfires",

      "totalCount": 126465,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1400178426000,

      "endDate": 1400353202000,

      "startDate": 1400178473000,

      "keywords": "wildfires, San Diego, SanDiego, SanDiegoFire",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37833022990094",

      "name": "378330",

      "totalCount": 0,

      "language": "",

      "curator": "emscls3",

      "collectionCreationDate": 1400182328000,

      "endDate": 1400205600000,

      "startDate": 1400182636000,

      "keywords": null,

      "geo": "7.24,93.67,8.00,94.43",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37835631481796",

      "name": "378356",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1400190819000,

      "endDate": 1400212800000,

      "startDate": 1400191043000,

      "keywords": null,

      "geo": "18.88,120.92,19.64,121.68",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37834831628869",

      "name": "378348",

      "totalCount": 0,

      "language": "",

      "curator": "emscls4",

      "collectionCreationDate": 1400190967000,

      "endDate": 1400212801000,

      "startDate": 1400191043000,

      "keywords": null,

      "geo": "8.91,121.75,9.73,122.57",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37835934709159",

      "name": "378359",

      "totalCount": 0,

      "language": "",

      "curator": "emscls2",

      "collectionCreationDate": 1400194047000,

      "endDate": 1400216421000,

      "startDate": 1400194646000,

      "keywords": null,

      "geo": "-8.03,-79.86,-7.15,-78.98",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37839451958089",

      "name": "378394",

      "totalCount": 3750,

      "language": "",

      "curator": "emscls3",

      "collectionCreationDate": 1400211296000,

      "endDate": 1400234405000,

      "startDate": 1400211465000,

      "keywords": null,

      "geo": "26.63,53.94,27.31,54.62",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37841562280779",

      "name": "378415",

      "totalCount": 155,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1400221619000,

      "endDate": 1400256002000,

      "startDate": 1400233219000,

      "keywords": "",

      "geo": "-17.68,-72.33,-16.84,-71.49",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37842262930368",

      "name": "378422",

      "totalCount": 50,

      "language": "",

      "curator": "emscls4",

      "collectionCreationDate": 1400222268000,

      "endDate": 1400245205000,

      "startDate": 1400222274000,

      "keywords": null,

      "geo": "28.97,67.40,29.77,68.20",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37847481989787",

      "name": "378474",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400241328000,

      "endDate": 1400263200000,

      "startDate": 1400241492000,

      "keywords": null,

      "geo": "-16.64,-173.85,-15.82,-173.03",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "37853098860215",

      "name": "378530",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400258199000,

      "endDate": 1400281201000,

      "startDate": 1400258309000,

      "keywords": null,

      "geo": "-37.06,-73.93,-36.22,-73.09",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378544103370615",

      "name": "378544",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400262708000,

      "endDate": 1400284800000,

      "startDate": 1400263112000,

      "keywords": null,

      "geo": "24.18,124.59,24.86,125.27",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378558105295730",

      "name": "378558",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400264633000,

      "endDate": 1400288400000,

      "startDate": 1400264914000,

      "keywords": null,

      "geo": "-2.91,138.52,-2.07,139.36",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378578113919117",

      "name": "378578",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400273257000,

      "endDate": 1400295600000,

      "startDate": 1400273320000,

      "keywords": null,

      "geo": "-6.96,146.71,-6.14,147.53",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378659150350845",

      "name": "378659",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400309689000,

      "endDate": 1400331600000,

      "startDate": 1400309946000,

      "keywords": null,

      "geo": "-2.56,120.51,-1.84,121.23",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378674159620895",

      "name": "378674",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400318959000,

      "endDate": 1400342409000,

      "startDate": 1400319552000,

      "keywords": null,

      "geo": "-20.32,-70.99,-19.06,-69.73",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378683161765391",

      "name": "378683",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400321104000,

      "endDate": 1400346000000,

      "startDate": 1400321353000,

      "keywords": null,

      "geo": "-1.77,98.89,-1.05,99.61",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378719172591417",

      "name": "378719",

      "totalCount": 405,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400331929000,

      "endDate": 1400356803000,

      "startDate": 1400332160000,

      "keywords": null,

      "geo": "28.93,59.45,29.59,60.11",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378753186047458",

      "name": "378753",

      "totalCount": 35,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400345385000,

      "endDate": 1400367605000,

      "startDate": 1400345969000,

      "keywords": null,

      "geo": "49.48,8.29,50.22,9.03",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378770195599139",

      "name": "378770",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400354937000,

      "endDate": 1400378400000,

      "startDate": 1400354976000,

      "keywords": null,

      "geo": "-3.72,134.94,-2.90,135.76",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378878255694756",

      "name": "378878",

      "totalCount": 58300,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400415033000,

      "endDate": 1400439600000,

      "startDate": 1400415623000,

      "keywords": null,

      "geo": "34.67,25.67,35.39,26.39",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378974301822137",

      "name": "378974",

      "totalCount": 33680,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400461160000,

      "endDate": 1400486400000,

      "startDate": 1400461249000,

      "keywords": null,

      "geo": "40.53,19.39,41.37,20.23",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "378991308373299",

      "name": "378991",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400467711000,

      "endDate": 1400490002000,

      "startDate": 1400467853000,

      "keywords": null,

      "geo": "-6.48,145.89,-5.58,146.79",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379022321046957",

      "name": "379022",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400480385000,

      "endDate": 1400504401000,

      "startDate": 1400480461000,

      "keywords": null,

      "geo": "26.05,141.98,26.87,142.80",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379039332638538",

      "name": "379039",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400491976000,

      "endDate": 1400515200000,

      "startDate": 1400492473000,

      "keywords": null,

      "geo": "-3.15,141.60,-2.39,142.36",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379072346619373",

      "name": "379072",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400505957000,

      "endDate": 1400529600000,

      "startDate": 1400506292000,

      "keywords": null,

      "geo": "-32.31,-67.35,-31.59,-66.63",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379110368643054",

      "name": "379110",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400527981000,

      "endDate": 1400551200000,

      "startDate": 1400528508000,

      "keywords": null,

      "geo": "15.05,-91.91,16.03,-90.93",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379132391370928",

      "name": "379132",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400550709000,

      "endDate": 1400572800000,

      "startDate": 1400550723000,

      "keywords": null,

      "geo": "17.73,-100.29,18.61,-99.41",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379163401894298",

      "name": "379163",

      "totalCount": 33690,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400561232000,

      "endDate": 1400583600000,

      "startDate": 1400561530000,

      "keywords": null,

      "geo": "40.60,19.48,41.28,20.16",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379178407605216",

      "name": "379178",

      "totalCount": 375,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400566943000,

      "endDate": 1400590803000,

      "startDate": 1400567534000,

      "keywords": null,

      "geo": "10.96,41.54,11.64,42.22",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379252439168078",

      "name": "379252",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400598506000,

      "endDate": 1400623200000,

      "startDate": 1400598755000,

      "keywords": null,

      "geo": "-4.63,122.45,-3.89,123.19",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379297469260651",

      "name": "379297",

      "totalCount": 32855,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400628598000,

      "endDate": 1400652000000,

      "startDate": 1400628772000,

      "keywords": null,

      "geo": "39.17,23.79,39.93,24.55",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379306472409338",

      "name": "379306",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400631748000,

      "endDate": 1400655600000,

      "startDate": 1400631774000,

      "keywords": null,

      "geo": "22.89,120.60,24.59,122.30",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379367496489291",

      "name": "379367",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400655827000,

      "endDate": 1400680800000,

      "startDate": 1400656388000,

      "keywords": null,

      "geo": "-5.61,147.56,-4.81,148.36",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379377499725736",

      "name": "379377",

      "totalCount": 400,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400659064000,

      "endDate": 1400684403000,

      "startDate": 1400659390000,

      "keywords": null,

      "geo": "35.31,14.52,35.99,15.20",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379382501354060",

      "name": "379382",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400660692000,

      "endDate": 1400684403000,

      "startDate": 1400661191000,

      "keywords": null,

      "geo": "-9.37,124.97,-8.61,125.73",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379388503994850",

      "name": "379388",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400663333000,

      "endDate": 1400688014000,

      "startDate": 1400663593000,

      "keywords": null,

      "geo": "-31.20,-72.27,-29.58,-70.65",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379406506513731",

      "name": "379406",

      "totalCount": 9750,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1400665851000,

      "endDate": 1400688018000,

      "startDate": 1400665995000,

      "keywords": null,

      "geo": "29.08,50.36,29.92,51.20",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379435510408927",

      "name": "379435",

      "totalCount": 12470,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1400669747000,

      "endDate": 1400695202000,

      "startDate": 1400670198000,

      "keywords": null,

      "geo": "30.51,47.67,31.35,48.51",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379451518757477",

      "name": "379451",

      "totalCount": 0,

      "language": "",

      "curator": "emscls4",

      "collectionCreationDate": 1400678096000,

      "endDate": 1400702400000,

      "startDate": 1400678604000,

      "keywords": null,

      "geo": "51.01,157.65,51.77,158.41",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379500536905195",

      "name": "379500",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400696243000,

      "endDate": 1400738400000,

      "startDate": 1400713612000,

      "keywords": null,

      "geo": "-3.32,127.55,-2.16,128.71",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379551562516768",

      "name": "379551",

      "totalCount": 6810,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400721855000,

      "endDate": 1400745601000,

      "startDate": 1400722014000,

      "keywords": null,

      "geo": "29.31,50.63,29.99,51.31",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379558567233400",

      "name": "379558",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400726571000,

      "endDate": 1400749200000,

      "startDate": 1400726817000,

      "keywords": null,

      "geo": "-4.68,122.39,-3.86,123.21",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379604577060265",

      "name": "379604",

      "totalCount": 1725,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400736398000,

      "endDate": 1400760006000,

      "startDate": 1400736422000,

      "keywords": null,

      "geo": "35.12,-0.19,35.88,0.57",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379606577193374",

      "name": "379606",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1400736531000,

      "endDate": 1400760007000,

      "startDate": 1400737023000,

      "keywords": null,

      "geo": "-11.63,161.08,-10.65,162.06",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379634590610979",

      "name": "379634",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400749949000,

      "endDate": 1400792400000,

      "startDate": 1400767729000,

      "keywords": null,

      "geo": "18.63,121.13,19.43,121.93",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379646600093881",

      "name": "379646",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400759432000,

      "endDate": 1400792400000,

      "startDate": 1400767707000,

      "keywords": null,

      "geo": "-1.99,134.52,-1.25,135.26",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379635610271384",

      "name": "379635",

      "totalCount": 11740,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400769610000,

      "endDate": 1400792407000,

      "startDate": 1400770040000,

      "keywords": null,

      "geo": "26.87,54.14,27.59,54.86",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379696625207810",

      "name": "379696",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400784546000,

      "endDate": 1400806800000,

      "startDate": 1400785048000,

      "keywords": null,

      "geo": "-4.99,133.18,-4.17,134.00",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379698629625422",

      "name": "379698",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1400788964000,

      "endDate": 1400814000000,

      "startDate": 1400789250000,

      "keywords": null,

      "geo": "1.78,96.08,2.60,96.90",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379703639755469",

      "name": "379703",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400799094000,

      "endDate": 1400821200000,

      "startDate": 1400799455000,

      "keywords": null,

      "geo": "8.82,121.95,9.64,122.77",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379778677361843",

      "name": "379778",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400836700000,

      "endDate": 1400860800000,

      "startDate": 1400837272000,

      "keywords": null,

      "geo": "-11.71,161.00,-10.63,162.08",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379811706799890",

      "name": "379811",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400866138000,

      "endDate": 1400889600000,

      "startDate": 1400866684000,

      "keywords": null,

      "geo": "23.22,121.22,23.88,121.88",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379815715133422",

      "name": "379815",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400874471000,

      "endDate": 1400896800000,

      "startDate": 1400874488000,

      "keywords": null,

      "geo": "55.64,113.51,56.72,114.59",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379829719011705",

      "name": "379829",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400878349000,

      "endDate": 1400900400000,

      "startDate": 1400878690000,

      "keywords": null,

      "geo": "24.20,97.15,25.72,98.67",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379911755109751",

      "name": "379911",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400914447000,

      "endDate": 1400936417000,

      "startDate": 1400914705000,

      "keywords": null,

      "geo": "-15.67,-74.28,-14.51,-73.12",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379920758046743",

      "name": "379920",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400917385000,

      "endDate": 1400940002000,

      "startDate": 1400917707000,

      "keywords": null,

      "geo": "30.21,34.83,30.87,35.49",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379926760769060",

      "name": "379926",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400920107000,

      "endDate": 1400943600000,

      "startDate": 1400920708000,

      "keywords": null,

      "geo": "15.72,-99.01,17.16,-97.57",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379935764085558",

      "name": "379935",

      "totalCount": 43155,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400923423000,

      "endDate": 1400947201000,

      "startDate": 1400923710000,

      "keywords": null,

      "geo": "39.53,24.63,41.05,26.15",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379942764799407",

      "name": "379942",

      "totalCount": 42045,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1400924137000,

      "endDate": 1400947202000,

      "startDate": 1400924310000,

      "keywords": null,

      "geo": "40.02,25.88,40.84,26.70",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379945764956174",

      "name": "379945",

      "totalCount": 41635,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1400924294000,

      "endDate": 1400947204000,

      "startDate": 1400924310000,

      "keywords": null,

      "geo": "40.06,25.75,40.96,26.65",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379949765585512",

      "name": "379949",

      "totalCount": 40700,

      "language": "",

      "curator": "emscls4",

      "collectionCreationDate": 1400924924000,

      "endDate": 1400947205000,

      "startDate": 1400925511000,

      "keywords": null,

      "geo": "39.68,23.91,40.30,24.53",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "379987767672082",

      "name": "379987",

      "totalCount": 45680,

      "language": "",

      "curator": "emscls3",

      "collectionCreationDate": 1400927010000,

      "endDate": 1400950801000,

      "startDate": 1400927313000,

      "keywords": null,

      "geo": "39.67,23.76,40.39,24.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380169783286511",

      "name": "380169",

      "totalCount": 55165,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400942625000,

      "endDate": 1400965200000,

      "startDate": 1400942926000,

      "keywords": null,

      "geo": "39.63,23.97,40.35,24.69",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380222788638492",

      "name": "380222",

      "totalCount": 79665,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400947976000,

      "endDate": 1400972400000,

      "startDate": 1400948331000,

      "keywords": null,

      "geo": "40.05,25.82,40.71,26.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380244790149255",

      "name": "380244",

      "totalCount": 77835,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400949488000,

      "endDate": 1400972401000,

      "startDate": 1400949532000,

      "keywords": null,

      "geo": "39.92,25.26,40.64,25.98",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380254790791136",

      "name": "380254",

      "totalCount": 76070,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400950129000,

      "endDate": 1400972401000,

      "startDate": 1400950132000,

      "keywords": null,

      "geo": "39.64,23.94,40.38,24.68",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380438813207968",

      "name": "380438",

      "totalCount": 42865,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1400972546000,

      "endDate": 1400997602000,

      "startDate": 1400972948000,

      "keywords": null,

      "geo": "40.03,25.57,40.65,26.19",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380473817111179",

      "name": "380473",

      "totalCount": 37835,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1400976449000,

      "endDate": 1401001200000,

      "startDate": 1400976550000,

      "keywords": null,

      "geo": "39.61,23.91,40.41,24.71",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380382820319590",

      "name": "380382",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1400979657000,

      "endDate": 1401004800000,

      "startDate": 1400980152000,

      "keywords": null,

      "geo": "-6.71,103.71,-5.63,104.79",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380547826999026",

      "name": "380547",

      "totalCount": 32695,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1400986337000,

      "endDate": 1401008401000,

      "startDate": 1400986755000,

      "keywords": null,

      "geo": "39.79,24.66,40.45,25.32",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380642837516392",

      "name": "380642",

      "totalCount": 35865,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1400996855000,

      "endDate": 1401019201000,

      "startDate": 1400996962000,

      "keywords": null,

      "geo": "40.08,25.73,40.74,26.39",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380666841815260",

      "name": "380666",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401001153000,

      "endDate": 1401022800000,

      "startDate": 1401001165000,

      "keywords": null,

      "geo": "18.92,120.63,19.72,121.43",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380737858598989",

      "name": "380737",

      "totalCount": 42585,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401017937000,

      "endDate": 1401040801000,

      "startDate": 1401017975000,

      "keywords": null,

      "geo": "40.06,25.74,40.80,26.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380745860082875",

      "name": "380745",

      "totalCount": 48975,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401019421000,

      "endDate": 1401044400000,

      "startDate": 1401019776000,

      "keywords": null,

      "geo": "40.00,25.73,40.74,26.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380765861517310",

      "name": "380765",

      "totalCount": 765,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401020855000,

      "endDate": 1401044406000,

      "startDate": 1401020977000,

      "keywords": null,

      "geo": "33.79,35.05,34.45,35.71",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380769862531746",

      "name": "380769",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401021870000,

      "endDate": 1401044407000,

      "startDate": 1401022177000,

      "keywords": null,

      "geo": "22.57,120.67,23.55,121.65",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380793870754324",

      "name": "380793",

      "totalCount": 60435,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401030093000,

      "endDate": 1401055202000,

      "startDate": 1401030583000,

      "keywords": null,

      "geo": "39.89,24.89,40.51,25.51",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380898898589549",

      "name": "380898",

      "totalCount": 35955,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401057927000,

      "endDate": 1401080400000,

      "startDate": 1401058199000,

      "keywords": null,

      "geo": "38.23,20.14,38.89,20.80",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380907898844288",

      "name": "380907",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401058183000,

      "endDate": 1401080401000,

      "startDate": 1401058199000,

      "keywords": null,

      "geo": "-1.33,121.63,-0.49,122.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380933913633560",

      "name": "380933",

      "totalCount": 44260,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401072971000,

      "endDate": 1401098400000,

      "startDate": 1401073206000,

      "keywords": null,

      "geo": "34.53,23.43,35.19,24.09",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380962926271063",

      "name": "380962",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401085609000,

      "endDate": 1401109200000,

      "startDate": 1401085813000,

      "keywords": null,

      "geo": "1.05,96.84,1.85,97.64",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "380991936937911",

      "name": "380991",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401096276000,

      "endDate": 1401120000000,

      "startDate": 1401096618000,

      "keywords": null,

      "geo": "0.73,119.77,1.53,120.57",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381002938700659",

      "name": "381002",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401098038000,

      "endDate": 1401123600000,

      "startDate": 1401098419000,

      "keywords": null,

      "geo": "-7.49,155.18,-6.59,156.08",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381037947970011",

      "name": "381037",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401107308000,

      "endDate": 1401130800000,

      "startDate": 1401107423000,

      "keywords": null,

      "geo": "2.12,95.35,2.96,96.19",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381025953127312",

      "name": "381025",

      "totalCount": 90,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401112465000,

      "endDate": 1401138001000,

      "startDate": 1401112825000,

      "keywords": null,

      "geo": "40.77,43.82,41.39,44.44",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381169980647276",

      "name": "381169",

      "totalCount": 46120,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401139985000,

      "endDate": 1401163200000,

      "startDate": 1401140438000,

      "keywords": null,

      "geo": "39.85,24.83,40.57,25.55",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381202990608690",

      "name": "381202",

      "totalCount": 3865,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401149946000,

      "endDate": 1401174002000,

      "startDate": 1401150043000,

      "keywords": null,

      "geo": "38.53,43.65,39.29,44.41",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381209993733601",

      "name": "381209",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401153071000,

      "endDate": 1401177610000,

      "startDate": 1401153645000,

      "keywords": null,

      "geo": "40.24,77.32,40.98,78.06",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "381213999279929",

      "name": "381213",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401158618000,

      "endDate": 1401181200000,

      "startDate": 1401159048000,

      "keywords": null,

      "geo": "40.23,77.27,40.97,78.01",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3812281002854478",

      "name": "381228",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401162193000,

      "endDate": 1401184811000,

      "startDate": 1401162650000,

      "keywords": null,

      "geo": "-21.35,-71.09,-20.37,-70.11",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3812591010391128",

      "name": "381259",

      "totalCount": 4370,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401169729000,

      "endDate": 1401192001000,

      "startDate": 1401169853000,

      "keywords": null,

      "geo": "26.03,55.38,26.93,56.28",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3813271022669270",

      "name": "381327",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401182007000,

      "endDate": 1401206402000,

      "startDate": 1401182460000,

      "keywords": null,

      "geo": "-36.64,-73.65,-35.66,-72.67",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3813451025910968",

      "name": "381345",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401185249000,

      "endDate": 1401210000000,

      "startDate": 1401185462000,

      "keywords": null,

      "geo": "-3.92,150.99,-3.10,151.81",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-05-protesto",

      "name": "Protesto",

      "totalCount": 996175,

      "language": "en,pt",

      "curator": "lsousapinheiro",

      "collectionCreationDate": 1401186717000,

      "endDate": 1401361200000,

      "startDate": 1401186731000,

      "keywords": "#protesto, #copa2014,",

      "geo": "-47.5,-23.4,-37.1,-17.8",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3815051076282424",

      "name": "381505",

      "totalCount": 3520,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401235621000,

      "endDate": 1401260403000,

      "startDate": 1401235889000,

      "keywords": null,

      "geo": "40.15,41.94,40.87,42.66",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3815411085395612",

      "name": "381541",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401244734000,

      "endDate": 1401267601000,

      "startDate": 1401244894000,

      "keywords": null,

      "geo": "42.37,74.29,43.17,75.09",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "3815771090430488",

      "name": "381577",

      "totalCount": 45870,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401249769000,

      "endDate": 1401274802000,

      "startDate": 1401250297000,

      "keywords": null,

      "geo": "40.06,25.72,40.78,26.44",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3815771105737847",

      "name": "EMSC-381577",

      "totalCount": 46385,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401265076000,

      "endDate": 1401289200000,

      "startDate": 1401265306000,

      "keywords": null,

      "geo": "40.07,25.80,40.79,26.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3816061107257765",

      "name": "EMSC-381606",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401266596000,

      "endDate": 1401289201000,

      "startDate": 1401267108000,

      "keywords": null,

      "geo": "-23.46,-71.08,-22.74,-70.36",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3816121107375633",

      "name": "EMSC-381612",

      "totalCount": 42230,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401266714000,

      "endDate": 1401289202000,

      "startDate": 1401267108000,

      "keywords": null,

      "geo": "39.54,24.13,40.20,24.79",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3816281115090730",

      "name": "EMSC-381628",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401274429000,

      "endDate": 1401300000000,

      "startDate": 1401274914000,

      "keywords": null,

      "geo": "-40.93,176.58,-40.11,177.40",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3816781137156451",

      "name": "EMSC-381678",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401296494000,

      "endDate": 1401321600000,

      "startDate": 1401296529000,

      "keywords": null,

      "geo": "21.34,121.15,22.50,122.31",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3817031152565384",

      "name": "EMSC-381703",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401311904000,

      "endDate": 1401336022000,

      "startDate": 1401312138000,

      "keywords": null,

      "geo": "17.42,-69.22,19.12,-67.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3817291168000330",

      "name": "EMSC-381729",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401327338000,

      "endDate": 1401350400000,

      "startDate": 1401327746000,

      "keywords": null,

      "geo": "33.00,138.94,33.82,139.76",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3817611181104819",

      "name": "EMSC-381761",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401340443000,

      "endDate": 1401364800000,

      "startDate": 1401340954000,

      "keywords": null,

      "geo": "70.43,139.63,71.23,140.43",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3817681181563282",

      "name": "EMSC-381768",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401340901000,

      "endDate": 1401364800000,

      "startDate": 1401340954000,

      "keywords": null,

      "geo": "49.31,155.39,50.21,156.29",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3817791183925064",

      "name": "EMSC-381779",

      "totalCount": 47140,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401343263000,

      "endDate": 1401368401000,

      "startDate": 1401343355000,

      "keywords": null,

      "geo": "39.71,24.12,40.33,24.74",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3817901186915701",

      "name": "EMSC-381790",

      "totalCount": 40900,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401346253000,

      "endDate": 1401368402000,

      "startDate": 1401346358000,

      "keywords": null,

      "geo": "39.72,24.10,40.34,24.72",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3818201191755030",

      "name": "EMSC-381820",

      "totalCount": 44115,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401351093000,

      "endDate": 1401375616000,

      "startDate": 1401351161000,

      "keywords": null,

      "geo": "39.73,24.10,40.35,24.72",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3818251193653392",

      "name": "EMSC-381825",

      "totalCount": 40040,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1401352992000,

      "endDate": 1401375617000,

      "startDate": 1401353563000,

      "keywords": null,

      "geo": "39.72,24.10,40.34,24.72",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3819181241928847",

      "name": "EMSC-381918",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401401267000,

      "endDate": 1401426000000,

      "startDate": 1401401588000,

      "keywords": null,

      "geo": "44.29,147.51,45.17,148.39",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3819311253744019",

      "name": "EMSC-381931",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401413082000,

      "endDate": 1401436800000,

      "startDate": 1401413592000,

      "keywords": null,

      "geo": "24.21,97.04,25.65,98.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3819351257938970",

      "name": "EMSC-381935",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401417277000,

      "endDate": 1401440400000,

      "startDate": 1401417793000,

      "keywords": null,

      "geo": "13.44,-91.61,14.26,-90.79",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3819451261732398",

      "name": "EMSC-381945",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401421071000,

      "endDate": 1401444000000,

      "startDate": 1401421395000,

      "keywords": null,

      "geo": "24.41,122.80,25.15,123.54",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3819531263602331",

      "name": "EMSC-381953",

      "totalCount": 40270,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401422941000,

      "endDate": 1401447600000,

      "startDate": 1401423195000,

      "keywords": null,

      "geo": "39.86,25.23,40.48,25.85",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3820311276802712",

      "name": "EMSC-382031",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401436141000,

      "endDate": 1401458400000,

      "startDate": 1401436401000,

      "keywords": null,

      "geo": "38.05,-119.73,38.77,-119.01",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3820451279524238",

      "name": "EMSC-382045",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401438862000,

      "endDate": 1401462000000,

      "startDate": 1401439403000,

      "keywords": null,

      "geo": "24.39,122.71,25.15,123.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3820681282877025",

      "name": "EMSC-382068",

      "totalCount": 6805,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401442215000,

      "endDate": 1401465601000,

      "startDate": 1401442404000,

      "keywords": null,

      "geo": "26.19,53.55,26.93,54.29",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3821191291177313",

      "name": "EMSC-382119",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401450515000,

      "endDate": 1401472811000,

      "startDate": 1401450808000,

      "keywords": null,

      "geo": "-21.95,-70.58,-20.69,-69.32",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3821481305130490",

      "name": "EMSC-382148",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401464468000,

      "endDate": 1401487200000,

      "startDate": 1401464614000,

      "keywords": null,

      "geo": "-21.82,-70.36,-20.56,-69.10",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3821441305262232",

      "name": "EMSC-382144",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401464601000,

      "endDate": 1401487201000,

      "startDate": 1401464614000,

      "keywords": null,

      "geo": "8.80,126.13,10.14,127.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3821531308365979",

      "name": "EMSC-382153",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401467704000,

      "endDate": 1401490800000,

      "startDate": 1401468215000,

      "keywords": null,

      "geo": "24.58,97.47,25.38,98.27",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3821601313213421",

      "name": "EMSC-382160",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401472551000,

      "endDate": 1401498000000,

      "startDate": 1401473017000,

      "keywords": null,

      "geo": "-7.85,105.49,-6.95,106.39",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3821921325611014",

      "name": "EMSC-382192",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401484949000,

      "endDate": 1401508800000,

      "startDate": 1401485022000,

      "keywords": null,

      "geo": "22.52,120.95,23.20,121.63",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3822051328812959",

      "name": "EMSC-382205",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401488151000,

      "endDate": 1401512404000,

      "startDate": 1401488623000,

      "keywords": null,

      "geo": "-34.12,-72.79,-32.78,-71.45",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3822171331876168",

      "name": "EMSC-382217",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401491214000,

      "endDate": 1401516000000,

      "startDate": 1401491625000,

      "keywords": null,

      "geo": "52.49,136.41,53.33,137.25",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3822441345396616",

      "name": "EMSC-382244",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401504734000,

      "endDate": 1401526800000,

      "startDate": 1401504830000,

      "keywords": null,

      "geo": "24.35,120.81,25.01,121.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3822711358076099",

      "name": "EMSC-382271",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401517414000,

      "endDate": 1401541200000,

      "startDate": 1401517434000,

      "keywords": null,

      "geo": "54.55,165.18,55.63,166.26",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3822931365351155",

      "name": "EMSC-382293",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401524689000,

      "endDate": 1401548401000,

      "startDate": 1401525237000,

      "keywords": null,

      "geo": "47.55,83.07,48.31,83.83",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3823341373458458",

      "name": "EMSC-382334",

      "totalCount": 305,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401532797000,

      "endDate": 1401555610000,

      "startDate": 1401533040000,

      "keywords": null,

      "geo": "49.81,12.04,50.55,12.78",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3823421374974030",

      "name": "EMSC-382342",

      "totalCount": 51655,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401534312000,

      "endDate": 1401559201000,

      "startDate": 1401534841000,

      "keywords": null,

      "geo": "49.39,18.05,50.11,18.77",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824121396302352",

      "name": "EMSC-382412",

      "totalCount": 9765,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401555641000,

      "endDate": 1401580806000,

      "startDate": 1401555851000,

      "keywords": null,

      "geo": "26.27,53.67,26.93,54.33",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824161398696226",

      "name": "EMSC-382416",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401558034000,

      "endDate": 1401580806000,

      "startDate": 1401558252000,

      "keywords": null,

      "geo": "16.24,95.38,17.06,96.20",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824331404783056",

      "name": "EMSC-382433",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401564121000,

      "endDate": 1401588000000,

      "startDate": 1401564254000,

      "keywords": null,

      "geo": "8.85,125.97,9.83,126.95",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824301405659622",

      "name": "EMSC-382430",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401564997000,

      "endDate": 1401588000000,

      "startDate": 1401565455000,

      "keywords": null,

      "geo": "8.89,126.05,9.97,127.13",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824321406476731",

      "name": "EMSC-382432",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401565815000,

      "endDate": 1401588001000,

      "startDate": 1401566055000,

      "keywords": null,

      "geo": "8.79,126.03,9.87,127.11",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824211412700428",

      "name": "EMSC-382421",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1401572038000,

      "endDate": 1401595200000,

      "startDate": 1401572058000,

      "keywords": null,

      "geo": "8.92,126.11,9.80,126.99",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3824861414856446",

      "name": "EMSC-382486",

      "totalCount": 50510,

      "language": "",

      "curator": "emscls4",

      "collectionCreationDate": 1401574195000,

      "endDate": 1401598801000,

      "startDate": 1401574459000,

      "keywords": null,

      "geo": "34.51,25.54,35.13,26.16",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3825261431244272",

      "name": "EMSC-382526",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401590582000,

      "endDate": 1401613206000,

      "startDate": 1401590667000,

      "keywords": null,

      "geo": "9.74,-73.36,10.90,-72.20",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3826091464985603",

      "name": "EMSC-382609",

      "totalCount": 57940,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401624324000,

      "endDate": 1401649200000,

      "startDate": 1401624879000,

      "keywords": null,

      "geo": "34.19,24.18,35.03,25.02",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3826381476664922",

      "name": "EMSC-382638",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401636003000,

      "endDate": 1401660000000,

      "startDate": 1401636284000,

      "keywords": null,

      "geo": "-7.05,146.42,-6.23,147.24",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3826421477490840",

      "name": "EMSC-382642",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401636829000,

      "endDate": 1401660000000,

      "startDate": 1401636884000,

      "keywords": null,

      "geo": "-2.71,138.68,-1.91,139.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3826751495465594",

      "name": "EMSC-382675",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401654804000,

      "endDate": 1401678000000,

      "startDate": 1401654891000,

      "keywords": null,

      "geo": "8.94,126.14,9.74,126.94",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827021498090700",

      "name": "EMSC-382702",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401657429000,

      "endDate": 1401681600000,

      "startDate": 1401657893000,

      "keywords": null,

      "geo": "1.87,95.81,2.69,96.63",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827261512444703",

      "name": "EMSC-382726",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401671783000,

      "endDate": 1401696017000,

      "startDate": 1401672298000,

      "keywords": null,

      "geo": "19.46,-71.61,20.22,-70.85",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827351515891628",

      "name": "EMSC-382735",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401675229000,

      "endDate": 1401699600000,

      "startDate": 1401675299000,

      "keywords": null,

      "geo": "-0.66,132.17,0.08,132.91",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827251516457876",

      "name": "EMSC-382725",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401675796000,

      "endDate": 1401699600000,

      "startDate": 1401675900000,

      "keywords": null,

      "geo": "9.15,126.04,10.05,126.94",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827471521962595",

      "name": "EMSC-382747",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401681301000,

      "endDate": 1401703200000,

      "startDate": 1401681302000,

      "keywords": null,

      "geo": "33.75,-118.83,34.43,-118.15",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827581528329996",

      "name": "EMSC-382758",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401687668000,

      "endDate": 1401710406000,

      "startDate": 1401687905000,

      "keywords": null,

      "geo": "-23.39,-71.21,-22.65,-70.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3827631529574008",

      "name": "EMSC-382763",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1401688912000,

      "endDate": 1401714012000,

      "startDate": 1401689105000,

      "keywords": null,

      "geo": "-34.16,-72.39,-33.40,-71.63",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3828191537297869",

      "name": "EMSC-382819",

      "totalCount": 135,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401696636000,

      "endDate": 1401746421000,

      "startDate": 1401723570000,

      "keywords": null,

      "geo": "43.26,45.24,44.02,46.00",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3828971568695407",

      "name": "EMSC-382897",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401728034000,

      "endDate": 1401750000000,

      "startDate": 1401728119000,

      "keywords": null,

      "geo": "60.19,170.77,60.95,171.53",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3829181579386294",

      "name": "EMSC-382918",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401738724000,

      "endDate": 1401760800000,

      "startDate": 1401738924000,

      "keywords": null,

      "geo": "20.84,121.70,21.56,122.42",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3829211581538602",

      "name": "EMSC-382921",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401740876000,

      "endDate": 1401764400000,

      "startDate": 1401741325000,

      "keywords": null,

      "geo": "21.61,120.60,22.29,121.28",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3829491590363169",

      "name": "EMSC-382949",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401749701000,

      "endDate": 1401771626000,

      "startDate": 1401749729000,

      "keywords": null,

      "geo": "27.41,56.89,28.15,57.63",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3829621593874584",

      "name": "EMSC-382962",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401753213000,

      "endDate": 1401775200000,

      "startDate": 1401753331000,

      "keywords": null,

      "geo": "40.02,-124.74,40.68,-124.08",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3829901611616587",

      "name": "EMSC-382990",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401770954000,

      "endDate": 1401793200000,

      "startDate": 1401771338000,

      "keywords": null,

      "geo": "-4.87,132.85,-4.05,133.67",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3830131616288112",

      "name": "EMSC-383013",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401775626000,

      "endDate": 1401800400000,

      "startDate": 1401776140000,

      "keywords": null,

      "geo": "24.50,97.42,25.24,98.16",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3830151617614920",

      "name": "EMSC-383015",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401776953000,

      "endDate": 1401800401000,

      "startDate": 1401777340000,

      "keywords": null,

      "geo": "67.47,-162.84,68.23,-162.08",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3830671638671729",

      "name": "EMSC-383067",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401798009000,

      "endDate": 1401822000000,

      "startDate": 1401798350000,

      "keywords": null,

      "geo": "-3.90,135.14,-3.16,135.88",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-qatar_data_sofiane",

      "name": "Qatar_Data_Sofiane",

      "totalCount": 131480,

      "language": "en,ar",

      "curator": "TabJnanou",

      "collectionCreationDate": 1401801178000,

      "endDate": 1401860277000,

      "startDate": 1401801200000,

      "keywords": "",

      "geo": "50.7122,24.5934,52.0604,26.2167",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3831411672330801",

      "name": "EMSC-383141",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401831669000,

      "endDate": 1401854400000,

      "startDate": 1401831968000,

      "keywords": null,

      "geo": "-13.01,-77.52,-12.03,-76.54",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3831741696223750",

      "name": "EMSC-383174",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401855562000,

      "endDate": 1402009200000,

      "startDate": 1401984795000,

      "keywords": null,

      "geo": "5.88,126.08,6.76,126.96",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3831851707746946",

      "name": "EMSC-383185",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401867085000,

      "endDate": 1402009201000,

      "startDate": 1401984774000,

      "keywords": null,

      "geo": "-30.18,137.08,-29.46,137.80",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3832511724169902",

      "name": "EMSC-383251",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401883508000,

      "endDate": 1401908400000,

      "startDate": 1401883592000,

      "keywords": null,

      "geo": "58.51,-137.40,60.13,-135.78",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833011742389748",

      "name": "EMSC-383301",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401901727000,

      "endDate": 1401926413000,

      "startDate": 1401902202000,

      "keywords": null,

      "geo": "-21.25,-71.45,-19.99,-70.19",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833421757648407",

      "name": "EMSC-383342",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401916987000,

      "endDate": 1401940812000,

      "startDate": 1401917209000,

      "keywords": null,

      "geo": "39.53,15.56,40.19,16.22",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833301766245655",

      "name": "EMSC-383330",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401925583000,

      "endDate": 1401948000000,

      "startDate": 1401925613000,

      "keywords": null,

      "geo": "19.70,121.11,20.46,121.87",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833631771018678",

      "name": "EMSC-383363",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1401930356000,

      "endDate": 1401955201000,

      "startDate": 1401930416000,

      "keywords": null,

      "geo": "11.99,-87.96,12.73,-87.22",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833681773338391",

      "name": "EMSC-383368",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1401932676000,

      "endDate": 1401955202000,

      "startDate": 1401932817000,

      "keywords": null,

      "geo": "14.19,145.20,15.07,146.08",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833791783385052",

      "name": "EMSC-383379",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401942723000,

      "endDate": 1401966012000,

      "startDate": 1401943023000,

      "keywords": null,

      "geo": "-21.21,-68.70,-20.55,-68.04",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3833961787966101",

      "name": "EMSC-383396",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1401947304000,

      "endDate": 1401969600000,

      "startDate": 1401947826000,

      "keywords": null,

      "geo": "60.90,-139.52,61.78,-138.64",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3834551808474737",

      "name": "EMSC-383455",

      "totalCount": 850,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1401967813000,

      "endDate": 1401978944000,

      "startDate": 1401977334000,

      "keywords": null,

      "geo": "34.80,33.65,35.42,34.27",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-3834751818800638",

      "name": "EMSC-383475",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1401978138000,

      "endDate": 1401978866000,

      "startDate": 1401978199000,

      "keywords": null,

      "geo": "-23.28,-70.90,-22.60,-70.22",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-brazil_world_cup",

      "name": "Brazil World Cup",

      "totalCount": 2960,

      "language": "en",

      "curator": "queenieweenie78",

      "collectionCreationDate": 1402322892000,

      "endDate": 1402560082000,

      "startDate": 1402560092000,

      "keywords": "#CopaDoMundo, #DilmaRoussef, #AgenciaPublica, #WorldCupForWhom, #NaoVaiTerCopa, #TherewillbenoWorldCup, #VaiTerCopa",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384403-2062405596",

      "name": "EMSC-384403",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402391900000,

      "endDate": 1402441200000,

      "startDate": 1402419451000,

      "keywords": null,

      "geo": "9.25,125.42,10.07,126.24",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "WFP2014",

      "name": "WFP",

      "totalCount": 10,

      "language": "en",

      "curator": "HQprice",

      "collectionCreationDate": 1402395001000,

      "endDate": 1402395289000,

      "startDate": 1402395037000,

      "keywords": "#WFP, #humanitarian, humanitarian, #foodaid",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384492-2028526294",

      "name": "EMSC-384492",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402425779000,

      "endDate": 1402448400000,

      "startDate": 1402426378000,

      "keywords": null,

      "geo": "-7.40,154.01,-6.14,155.27",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384502-2025837619",

      "name": "EMSC-384502",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402428467000,

      "endDate": 1402452000000,

      "startDate": 1402428780000,

      "keywords": null,

      "geo": "-2.28,127.88,-1.46,128.70",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384554-2008546058",

      "name": "EMSC-384554",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402445759000,

      "endDate": 1402470000000,

      "startDate": 1402446195000,

      "keywords": null,

      "geo": "-3.16,129.62,-2.48,130.30",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384608-1987125962",

      "name": "EMSC-384608",

      "totalCount": 30,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402467179000,

      "endDate": 1402491605000,

      "startDate": 1402467214000,

      "keywords": null,

      "geo": "28.10,56.71,28.76,57.37",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384613-1982101523",

      "name": "EMSC-384613",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402472203000,

      "endDate": 1402495200000,

      "startDate": 1402472619000,

      "keywords": null,

      "geo": "26.85,128.83,27.93,129.91",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384628-1976493415",

      "name": "EMSC-384628",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402477812000,

      "endDate": 1402560000000,

      "startDate": 1402478025000,

      "keywords": null,

      "geo": "9.13,125.43,10.11,126.41",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384633-1974992160",

      "name": "EMSC-384633",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1402479313000,

      "endDate": 1402560000000,

      "startDate": 1402479826000,

      "keywords": null,

      "geo": "34.69,-117.23,35.31,-116.61",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384654-1967984836",

      "name": "EMSC-384654",

      "totalCount": 450,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1402486320000,

      "endDate": 1402560000000,

      "startDate": 1402486433000,

      "keywords": null,

      "geo": "31.26,59.10,31.88,59.72",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384666-1963749042",

      "name": "EMSC-384666",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1402490556000,

      "endDate": 1402560000000,

      "startDate": 1402490638000,

      "keywords": null,

      "geo": "-3.94,127.00,-3.22,127.72",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Sandy",

      "name": "Landslide kazbegi",

      "totalCount": 5,

      "language": "en,ka",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1402491776000,

      "endDate": 1402434000000,

      "startDate": 1402491877000,

      "keywords": "#landslide, #georgia, #kazbegi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "kazbegi2014",

      "name": "Kazbegi Landslide",

      "totalCount": 170,

      "language": "en,ka",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1402492003000,

      "endDate": 1402495990000,

      "startDate": 1402495907000,

      "keywords": "#kazbegi, #landslid, #georgia",

      "geo": "39.88,40.95,47.01,43.81",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "georgialandslide2014",

      "name": "kazbegi landslide 3",

      "totalCount": 0,

      "language": "en",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1402496241000,

      "endDate": null,

      "startDate": 1402496258000,

      "keywords": "#kazbegi, #kazbegilandslide, #landslide2014, #gorgialandslide",

      "geo": "39.88,40.95,47.01,43.81",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "test1",

      "name": "Test 1",

      "totalCount": 0,

      "language": "en",

      "curator": "HQprice",

      "collectionCreationDate": 1402562612000,

      "endDate": 1402563379000,

      "startDate": 1402562629000,

      "keywords": "HQprice",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Test1.5",

      "name": "Test 1.5",

      "totalCount": 0,

      "language": "en",

      "curator": "HQprice",

      "collectionCreationDate": 1402562810000,

      "endDate": 1402642090000,

      "startDate": 1402642096000,

      "keywords": "@HQprice, HQprice",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384826-1887668340",

      "name": "EMSC-384826",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402566637000,

      "endDate": 1402592413000,

      "startDate": 1402567954000,

      "keywords": null,

      "geo": "3.63,-74.11,4.37,-73.37",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384849-1883217528",

      "name": "EMSC-384849",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402571088000,

      "endDate": 1402596000000,

      "startDate": 1402571595000,

      "keywords": null,

      "geo": "-14.12,166.16,-13.28,167.00",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384871-1877555273",

      "name": "EMSC-384871",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402576750000,

      "endDate": 1402599611000,

      "startDate": 1402577002000,

      "keywords": null,

      "geo": "-31.12,-72.09,-30.24,-71.21",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384876-1870227644",

      "name": "EMSC-384876",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1402584077000,

      "endDate": 1402606800000,

      "startDate": 1402584312000,

      "keywords": null,

      "geo": "-2.80,138.62,-2.14,139.28",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-terra_dei_fuochi",

      "name": "Terra dei Fuochi",

      "totalCount": 15965,

      "language": "en,it",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1402585734000,

      "endDate": 1426453200000,

      "startDate": 1426515064000,

      "keywords": "#Terra dei fuochi, #roghicampania, #rifiuticampania, #immondiziacampania",

      "geo": "13.846841,40.716838,14.846756,41.189503",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384935-1838085156",

      "name": "EMSC-384935",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402616220000,

      "endDate": 1402639200000,

      "startDate": 1402616715000,

      "keywords": null,

      "geo": "28.03,-111.91,28.65,-111.29",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384949-1834755789",

      "name": "EMSC-384949",

      "totalCount": 36315,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402619549000,

      "endDate": 1402642801000,

      "startDate": 1402619719000,

      "keywords": null,

      "geo": "34.84,26.56,35.82,27.54",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-384991-1815298756",

      "name": "EMSC-384991",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402639006000,

      "endDate": 1402664403000,

      "startDate": 1402639568000,

      "keywords": null,

      "geo": "-28.00,-71.67,-27.18,-70.85",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385004-1813858608",

      "name": "EMSC-385004",

      "totalCount": 35,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402640447000,

      "endDate": 1402664407000,

      "startDate": 1402640771000,

      "keywords": null,

      "geo": "27.19,65.62,28.17,66.60",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385028-1803464159",

      "name": "EMSC-385028",

      "totalCount": 44025,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402650841000,

      "endDate": 1402675200000,

      "startDate": 1402650998000,

      "keywords": null,

      "geo": "38.35,20.29,39.03,20.97",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385030-1803107692",

      "name": "EMSC-385030",

      "totalCount": 7015,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1402651198000,

      "endDate": 1402675201000,

      "startDate": 1402651600000,

      "keywords": null,

      "geo": "36.47,54.78,37.21,55.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385050-1791916639",

      "name": "EMSC-385050",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1402662388000,

      "endDate": 1402686019000,

      "startDate": 1402662428000,

      "keywords": null,

      "geo": "41.88,76.65,42.64,77.41",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385052-1791782119",

      "name": "EMSC-385052",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1402662523000,

      "endDate": 1402686019000,

      "startDate": 1402663030000,

      "keywords": null,

      "geo": "14.00,-91.67,15.08,-90.59",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385071-1787630072",

      "name": "EMSC-385071",

      "totalCount": 10,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402666676000,

      "endDate": 1402689606000,

      "startDate": 1402666923000,

      "keywords": null,

      "geo": "32.80,75.31,33.64,76.15",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-copa_do_mundo_test_brasilia",

      "name": "Copa do Mundo Test Brasilia",

      "totalCount": 0,

      "language": "en",

      "curator": "colomborobert",

      "collectionCreationDate": 1402672048000,

      "endDate": 1402761607000,

      "startDate": 1402672805000,

      "keywords": "#vaitercopa #holigans #barrabraba #torcedor #porrada",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385092-1778981560",

      "name": "EMSC-385092",

      "totalCount": 5,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402675323000,

      "endDate": 1402700414000,

      "startDate": 1402675662000,

      "keywords": null,

      "geo": "38.38,42.89,39.00,43.51",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385119-1766579562",

      "name": "EMSC-385119",

      "totalCount": 65300,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402687726000,

      "endDate": 1402711200000,

      "startDate": 1402688305000,

      "keywords": null,

      "geo": "43.61,16.69,44.23,17.31",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385131-1765138904",

      "name": "EMSC-385131",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1402689167000,

      "endDate": 1402711207000,

      "startDate": 1402689508000,

      "keywords": null,

      "geo": "29.00,85.77,29.74,86.51",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385151-1753815021",

      "name": "EMSC-385151",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402700491000,

      "endDate": 1402725600000,

      "startDate": 1402700969000,

      "keywords": null,

      "geo": "-6.96,147.24,-5.80,148.40",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385235-1702533804",

      "name": "EMSC-385235",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402751771000,

      "endDate": 1402776000000,

      "startDate": 1402752101000,

      "keywords": null,

      "geo": "-11.76,160.81,-10.60,161.97",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385296-1669855525",

      "name": "EMSC-385296",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402784450000,

      "endDate": 1402808400000,

      "startDate": 1402784584000,

      "keywords": null,

      "geo": "-25.85,-69.52,-24.33,-68.00",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385298-1669738730",

      "name": "EMSC-385298",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402784566000,

      "endDate": 1402808401000,

      "startDate": 1402784584000,

      "keywords": null,

      "geo": "23.38,121.13,24.12,121.87",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385370-1612009641",

      "name": "EMSC-385370",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402842295000,

      "endDate": 1402866008000,

      "startDate": 1402842589000,

      "keywords": null,

      "geo": "-27.43,25.99,-26.35,27.07",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385398-1597565597",

      "name": "EMSC-385398",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402856739000,

      "endDate": 1402880400000,

      "startDate": 1402857036000,

      "keywords": null,

      "geo": "36.06,140.92,37.40,142.26",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385409-1592619726",

      "name": "EMSC-385409",

      "totalCount": 57730,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402861685000,

      "endDate": 1402884000000,

      "startDate": 1402861874000,

      "keywords": null,

      "geo": "39.89,24.84,40.55,25.50",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385411-1590763252",

      "name": "EMSC-385411",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1402863542000,

      "endDate": 1402887602000,

      "startDate": 1402863689000,

      "keywords": null,

      "geo": "36.46,140.24,37.90,141.68",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385432-1576152218",

      "name": "EMSC-385432",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402878153000,

      "endDate": 1402902000000,

      "startDate": 1402878220000,

      "keywords": null,

      "geo": "-5.78,141.04,-4.98,141.84",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385443-1566760727",

      "name": "EMSC-385443",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402887545000,

      "endDate": 1402912800000,

      "startDate": 1402887879000,

      "keywords": null,

      "geo": "31.44,59.85,32.06,60.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385469-1553419171",

      "name": "EMSC-385469",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402900886000,

      "endDate": null,

      "startDate": 1402901141000,

      "keywords": null,

      "geo": "0.98,-79.92,2.24,-78.66",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385494-1542789923",

      "name": "EMSC-385494",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402911516000,

      "endDate": null,

      "startDate": 1402911635000,

      "keywords": null,

      "geo": "-4.16,128.46,-3.34,129.28",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385501-1541106706",

      "name": "EMSC-385501",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402913198000,

      "endDate": 1402938000000,

      "startDate": 1402914668000,

      "keywords": null,

      "geo": "14.78,-92.58,15.94,-91.42",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Iraqprices",

      "name": "Iraq crisis prices",

      "totalCount": 4055,

      "language": "en",

      "curator": "test_fam1",

      "collectionCreationDate": 1402913556000,

      "endDate": 1402919523000,

      "startDate": 1402915037000,

      "keywords": "iraq prices, iraq food, foodprices, crisis",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385505-1537972291",

      "name": "EMSC-385505",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402916333000,

      "endDate": 1402941600000,

      "startDate": 1402916471000,

      "keywords": null,

      "geo": "35.31,-97.64,36.07,-96.88",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-06-ukraine_vs_russia",

      "name": "Ukraine vs russia",

      "totalCount": 0,

      "language": "en,,ru,uk",

      "curator": "JisunAn",

      "collectionCreationDate": 1402919230000,

      "endDate": null,

      "startDate": 1402919243000,

      "keywords": "#ukraine",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Iraqcrisis",

      "name": "Iraq crisis",

      "totalCount": 385,

      "language": "en",

      "curator": "test_fam1",

      "collectionCreationDate": 1402919631000,

      "endDate": 1402923149000,

      "startDate": 1402919645000,

      "keywords": "Iraq prices, Iraq food, Iraq crisis food, Iraq market oil, Iraq oil",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385525-1532817773",

      "name": "EMSC-385525",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1402921488000,

      "endDate": 1402945200000,

      "startDate": 1402921882000,

      "keywords": null,

      "geo": "67.02,-162.85,68.36,-161.51",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "kenyaprice",

      "name": "Kenyaprice",

      "totalCount": 0,

      "language": "en",

      "curator": "test_fam1",

      "collectionCreationDate": 1402923129000,

      "endDate": null,

      "startDate": 1402923154000,

      "keywords": "Kenya price, kenya food price, kenya food production",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385619-1493156612",

      "name": "EMSC-385619",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1402961149000,

      "endDate": 1402984800000,

      "startDate": 1402961603000,

      "keywords": null,

      "geo": "2.24,95.78,3.08,96.62",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385629-1489298409",

      "name": "EMSC-385629",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1402965007000,

      "endDate": 1402988401000,

      "startDate": 1402965214000,

      "keywords": null,

      "geo": "-3.22,140.78,-2.42,141.58",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-385711-1443995498",

      "name": "EMSC-385711",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1403010310000,

      "endDate": null,

      "startDate": 1403010743000,

      "keywords": null,

      "geo": "27.95,51.33,28.63,52.01",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389808365639503",

      "name": "EMSC-389808",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1404819944000,

      "endDate": 1404820639000,

      "startDate": 1404819992000,

      "keywords": null,

      "geo": "42.03,140.78,43.37,142.12",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389860374372161",

      "name": "EMSC-389860",

      "totalCount": 5,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1404828677000,

      "endDate": 1404853200000,

      "startDate": 1404828993000,

      "keywords": null,

      "geo": "35.57,23.17,36.33,23.93",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389846375065217",

      "name": "EMSC-389846",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1404829371000,

      "endDate": 1404853201000,

      "startDate": 1404829594000,

      "keywords": null,

      "geo": "-18.83,167.23,-16.49,169.57",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389852375550017",

      "name": "EMSC-389852",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1404829856000,

      "endDate": 1404853201000,

      "startDate": 1404830194000,

      "keywords": null,

      "geo": "-34.14,-72.45,-33.38,-71.69",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389879382027503",

      "name": "EMSC-389879",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1404836333000,

      "endDate": 1404860400000,

      "startDate": 1404836796000,

      "keywords": null,

      "geo": "-21.18,-70.92,-20.38,-70.12",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389895390858610",

      "name": "EMSC-389895",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1404845164000,

      "endDate": 1404867600000,

      "startDate": 1404845199000,

      "keywords": null,

      "geo": "4.41,95.72,5.25,96.56",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389905396271008",

      "name": "EMSC-389905",

      "totalCount": 0,

      "language": "",

      "curator": "emscls5",

      "collectionCreationDate": 1404850576000,

      "endDate": 1404874800000,

      "startDate": 1404850601000,

      "keywords": null,

      "geo": "-3.74,142.72,-2.76,143.70",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389931405638934",

      "name": "EMSC-389931",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1404859944000,

      "endDate": null,

      "startDate": 1404860204000,

      "keywords": null,

      "geo": "39.00,77.92,39.90,78.82",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-389949410149058",

      "name": "EMSC-389949",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1404864455000,

      "endDate": null,

      "startDate": 1404865005000,

      "keywords": null,

      "geo": "-7.41,104.38,-6.51,105.28",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390017442613516",

      "name": "EMSC-390017",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1404896919000,

      "endDate": null,

      "startDate": 1404897413000,

      "keywords": null,

      "geo": "-0.84,122.79,-0.12,123.51",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-test_vm",

      "name": "test_vm",

      "totalCount": 65,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1404906643000,

      "endDate": 1404931238000,

      "startDate": 1404931226000,

      "keywords": "uk",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390063465154416",

      "name": "EMSC-390063",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1404919459000,

      "endDate": 1404943200000,

      "startDate": 1404919465000,

      "keywords": null,

      "geo": "-6.97,146.61,-6.13,147.45",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390067472770952",

      "name": "EMSC-390067",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1404927076000,

      "endDate": 1404950402000,

      "startDate": 1404927267000,

      "keywords": null,

      "geo": "-17.37,-72.39,-16.11,-71.13",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390074476855026",

      "name": "EMSC-390074",

      "totalCount": 3970,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1404931161000,

      "endDate": 1404954000000,

      "startDate": 1404931470000,

      "keywords": null,

      "geo": "36.39,-4.43,37.37,-3.45",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390109495604794",

      "name": "EMSC-390109",

      "totalCount": 85,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1404949910000,

      "endDate": 1404972000000,

      "startDate": 1404950076000,

      "keywords": null,

      "geo": "40.06,25.91,40.78,26.63",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390140523231095",

      "name": "EMSC-390140",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1404977536000,

      "endDate": 1405000809000,

      "startDate": 1404977684000,

      "keywords": null,

      "geo": "9.78,-85.72,10.52,-84.98",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390173525459188",

      "name": "EMSC-390173",

      "totalCount": 300,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1404979765000,

      "endDate": 1405004401000,

      "startDate": 1404980085000,

      "keywords": null,

      "geo": "35.69,29.87,36.31,30.49",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390181529214054",

      "name": "EMSC-390181",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1404983520000,

      "endDate": 1405008000000,

      "startDate": 1404983739000,

      "keywords": null,

      "geo": "36.51,139.98,37.39,140.86",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390261563045962",

      "name": "EMSC-390261",

      "totalCount": 435,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405017351000,

      "endDate": 1405040400000,

      "startDate": 1405017927000,

      "keywords": null,

      "geo": "36.08,26.68,36.80,27.40",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390276569492244",

      "name": "EMSC-390276",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405023797000,

      "endDate": 1405047600000,

      "startDate": 1405023929000,

      "keywords": null,

      "geo": "-7.01,154.74,-6.25,155.50",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390315592572451",

      "name": "EMSC-390315",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405046878000,

      "endDate": 1405069218000,

      "startDate": 1405047337000,

      "keywords": null,

      "geo": "18.67,-69.60,19.35,-68.92",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390352617805809",

      "name": "EMSC-390352",

      "totalCount": 11625,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405072111000,

      "endDate": 1405094401000,

      "startDate": 1405072544000,

      "keywords": null,

      "geo": "38.12,23.32,38.80,24.00",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390357618430884",

      "name": "EMSC-390357",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405072736000,

      "endDate": 1405098000000,

      "startDate": 1405073144000,

      "keywords": null,

      "geo": "-20.47,-174.69,-19.57,-173.79",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390387625778077",

      "name": "EMSC-390387",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405080083000,

      "endDate": 1405105213000,

      "startDate": 1405080347000,

      "keywords": null,

      "geo": "48.66,-2.84,49.46,-2.04",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390396628656949",

      "name": "EMSC-390396",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1405082962000,

      "endDate": 1405105214000,

      "startDate": 1405083348000,

      "keywords": null,

      "geo": "0.71,121.80,1.45,122.54",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390403630675531",

      "name": "EMSC-390403",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1405084981000,

      "endDate": 1405108807000,

      "startDate": 1405085149000,

      "keywords": null,

      "geo": "8.53,-78.26,9.27,-77.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390445652741738",

      "name": "EMSC-390445",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405107047000,

      "endDate": 1405130400000,

      "startDate": 1405107358000,

      "keywords": null,

      "geo": "35.42,140.55,38.84,143.97",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390493679495076",

      "name": "EMSC-390493",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405133800000,

      "endDate": 1405159200000,

      "startDate": 1405134367000,

      "keywords": null,

      "geo": "32.22,-109.43,32.88,-108.77",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390508688930267",

      "name": "EMSC-390508",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405143235000,

      "endDate": 1405166400000,

      "startDate": 1405143370000,

      "keywords": null,

      "geo": "13.44,-92.10,14.24,-91.30",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390511694087297",

      "name": "EMSC-390511",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405148393000,

      "endDate": 1405173600000,

      "startDate": 1405148772000,

      "keywords": null,

      "geo": "0.89,96.76,1.65,97.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390542710513624",

      "name": "EMSC-390542",

      "totalCount": 11975,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405164819000,

      "endDate": 1405188000000,

      "startDate": 1405164978000,

      "keywords": null,

      "geo": "34.72,47.61,35.40,48.29",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390562721503525",

      "name": "EMSC-390562",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405175809000,

      "endDate": 1405198810000,

      "startDate": 1405176382000,

      "keywords": null,

      "geo": "-15.71,-76.13,-14.73,-75.15",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390589728660295",

      "name": "EMSC-390589",

      "totalCount": 17165,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405182965000,

      "endDate": 1405206001000,

      "startDate": 1405182985000,

      "keywords": null,

      "geo": "32.49,48.08,33.21,48.80",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390595731357220",

      "name": "EMSC-390595",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1405185663000,

      "endDate": 1405209600000,

      "startDate": 1405185827000,

      "keywords": null,

      "geo": "35.51,-97.63,36.23,-96.91",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390612739105621",

      "name": "EMSC-390612",

      "totalCount": 4995,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405193411000,

      "endDate": 1405216804000,

      "startDate": 1405193631000,

      "keywords": null,

      "geo": "36.13,26.80,36.79,27.46",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390644748578716",

      "name": "EMSC-390644",

      "totalCount": 13650,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405202884000,

      "endDate": 1405227604000,

      "startDate": 1405203236000,

      "keywords": null,

      "geo": "36.27,35.55,36.89,36.17",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390688767884301",

      "name": "EMSC-390688",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405222190000,

      "endDate": 1405245600000,

      "startDate": 1405222444000,

      "keywords": null,

      "geo": "-33.62,-71.90,-32.36,-70.64",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390703776123667",

      "name": "EMSC-390703",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405230429000,

      "endDate": 1405252800000,

      "startDate": 1405230847000,

      "keywords": null,

      "geo": "55.28,165.80,55.90,166.42",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390715782287914",

      "name": "EMSC-390715",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405236594000,

      "endDate": 1405260009000,

      "startDate": 1405236850000,

      "keywords": null,

      "geo": "-31.34,-71.04,-30.00,-69.70",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390738796226076",

      "name": "EMSC-390738",

      "totalCount": 9600,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405250532000,

      "endDate": 1405274404000,

      "startDate": 1405250655000,

      "keywords": null,

      "geo": "40.50,20.99,41.16,21.65",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390759805852881",

      "name": "EMSC-390759",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405260158000,

      "endDate": 1405285218000,

      "startDate": 1405260259000,

      "keywords": null,

      "geo": "-15.64,-73.10,-14.98,-72.44",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390769810081733",

      "name": "EMSC-390769",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405264387000,

      "endDate": 1405288800000,

      "startDate": 1405264460000,

      "keywords": null,

      "geo": "-2.59,99.68,-1.75,100.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390785814809308",

      "name": "EMSC-390785",

      "totalCount": 0,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1405269115000,

      "endDate": 1405292420000,

      "startDate": 1405269262000,

      "keywords": null,

      "geo": "36.02,67.38,37.18,68.54",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390799823071296",

      "name": "EMSC-390799",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405277377000,

      "endDate": 1405299600000,

      "startDate": 1405277666000,

      "keywords": null,

      "geo": "-7.87,154.92,-7.05,155.74",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390817827714864",

      "name": "EMSC-390817",

      "totalCount": 0,

      "language": "",

      "curator": "emscls6",

      "collectionCreationDate": 1405282020000,

      "endDate": 1405306800000,

      "startDate": 1405282468000,

      "keywords": null,

      "geo": "-4.86,150.56,-3.34,152.08",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390824831005632",

      "name": "EMSC-390824",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405285311000,

      "endDate": 1405310419000,

      "startDate": 1405285469000,

      "keywords": null,

      "geo": "-20.85,-71.01,-19.59,-69.75",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390854848042168",

      "name": "EMSC-390854",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405302348000,

      "endDate": 1405324800000,

      "startDate": 1405302876000,

      "keywords": null,

      "geo": "23.56,120.73,24.30,121.47",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390884861637847",

      "name": "EMSC-390884",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405315943000,

      "endDate": 1405329886000,

      "startDate": 1405316080000,

      "keywords": null,

      "geo": "-9.30,110.61,-7.78,112.13",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-390908870864861",

      "name": "EMSC-390908",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405325170000,

      "endDate": 1405335464000,

      "startDate": 1405330757000,

      "keywords": null,

      "geo": "4.83,125.54,6.63,127.34",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-test_collection",

      "name": "Test Collection",

      "totalCount": 0,

      "language": "en",

      "curator": "YakubenkovaH",

      "collectionCreationDate": 1405375467000,

      "endDate": 1405376288000,

      "startDate": 1405376226000,

      "keywords": "avalanche slab, avalanche rock, avalanche rocks, avalanche snow, avalanche snowpack, avalanche ice, avalanche mud, avalanche debris, avalanche die, avalanche dead, avalanche injured, avalanche earthquake, rockslide, snowslide, snowslip, mudslide -cream -icecream, landslide rock, landslide rocks, landslide mud, landslide debris, landslide ground, landslide terrain, landslide slope, landslide die, landslide dead, landslide injured, landslide earthquake, \"rock fall\", glissement terrain, glissement terrains, glissement terre, glissement sol, glissement lave, glissement glacier, glissement pierres, glissement séisme, glissement mort, glissement morts, glissement meurent, glissement blessés, glissement blessé, glissement tremblement terre, chute terrain, chute terrains, chute terre, chute sol, chute lave, chute glacier, chute pierres, chute séisme, chute mort, chute morts, chute meurent, chute blessés, chute blessé, chute tremblement terre, éboulement tranchée, éboulement tranche, avalancha ",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-one_more_collection",

      "name": "One more collection",

      "totalCount": 0,

      "language": "en",

      "curator": "YakubenkovaH",

      "collectionCreationDate": 1405376365000,

      "endDate": 1405376545000,

      "startDate": 1405376496000,

      "keywords": "avalanche slab, avalanche rock, avalanche rocks, avalanche snow, avalanche snowpack, avalanche ice, avalanche mud, avalanche debris, avalanche die, avalanche dead, avalanche injured, avalanche earthquake, rockslide, snowslide, snowslip, mudslide -cream -icecream, landslide rock, landslide rocks, landslide mud, landslide debris, landslide ground, landslide terrain, landslide slope, landslide die, landslide dead, landslide injured, landslide earthquake, \"rock fall\", glissement terrain, glissement terrains, glissement terre, glissement sol, glissement lave, glissement glacier, glissement pierres, glissement séisme, glissement mort, glissement morts, glissement meurent, glissement blessés, glissement blessé, glissement tremblement terre, chute terrain, chute terrains, chute terre, chute sol, chute lave, chute glacier, chute pierres, chute séisme, chute mort, chute morts, chute meurent, chute blessés, chute blessé, chute tremblement terre, éboulement tranchée, éboulement tranche, avalancha ",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC-391037931549762",

      "name": "EMSC-391037",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405385855000,

      "endDate": 1405420473000,

      "startDate": 1405415918000,

      "keywords": null,

      "geo": "36.49,54.25,37.11,54.87",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_391136975320540",

      "name": "EMSC_391136",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405429626000,

      "endDate": 1405454400000,

      "startDate": 1405430117000,

      "keywords": null,

      "geo": "162.59,55.70,163.43,56.54",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_391159988130380",

      "name": "EMSC_391159",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405442436000,

      "endDate": 1405465200000,

      "startDate": 1405442720000,

      "keywords": null,

      "geo": "114.00,54.86,114.62,55.48",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_391161988431189",

      "name": "EMSC_391161",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405442736000,

      "endDate": 1405465200000,

      "startDate": 1405443320000,

      "keywords": null,

      "geo": "150.79,-4.68,152.05,-3.42",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-moscow-metro",

      "name": "Moscow-metro-incident",

      "totalCount": 345,

      "language": "en",

      "curator": "IrinaTemnikova",

      "collectionCreationDate": 1405444842000,

      "endDate": 1405500102000,

      "startDate": 1405499500000,

      "keywords": "#подземка, москва, moscow, derailment, train derailment, metro derailment, #metro, #метро, #авария, #метроавария, #аварияметро, #авариявметро, #москва, #moscowsubway, #derailment, #Молодежная, #СлавянскийБульвар, #ПаркПобеды, #помощьпострадавшим, #происшествие, derails",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3912171031297506",

      "name": "EMSC_391217",

      "totalCount": 75,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405485602000,

      "endDate": 1405522800000,

      "startDate": 1405500215000,

      "keywords": null,

      "geo": "85.71,36.91,86.33,37.53",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3912491049818399",

      "name": "EMSC_391249",

      "totalCount": 4365,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405504123000,

      "endDate": 1405526402000,

      "startDate": 1405504643000,

      "keywords": null,

      "geo": "50.77,28.91,51.53,29.67",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3912591054599996",

      "name": "EMSC_391259",

      "totalCount": 0,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405508905000,

      "endDate": 1405533600000,

      "startDate": 1405509445000,

      "keywords": null,

      "geo": "-175.59,-22.72,-174.25,-21.38",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3912661055828364",

      "name": "EMSC_391266",

      "totalCount": 5,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1405510133000,

      "endDate": 1405533600000,

      "startDate": 1405510646000,

      "keywords": null,

      "geo": "150.96,-4.60,151.80,-3.76",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-mers_v2",

      "name": "Middle East Respiratory Syndrome-v2",

      "totalCount": 830,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1405529165000,

      "endDate": 1405583708000,

      "startDate": 1405529405000,

      "keywords": "coronavirus, corona virus, mers avian flu, mers camel, mers case, mers cases, mers contact, mers concerned, mers concerns, mers cov, mers-cov, mers deadly, mers death, mers disease, mers drugs, mers epidemic, mers genome, mers h7n9, mers haj, mers health, mers healthcare, mers hospital, mers infection, mers infects, mers medicine, mers outbreak, mers paper, mers patient, mers patients, mers precaution, mers precautionary, mers research, mers samples, mers saudi, mers saudi arabia, mers science, mers severe, mers sick, mers spread, mers spreads, mers spreading, mers studies, mers syndrome, mers toll, mers vaccine, mers vaccines, mers virus, mers worry, mers worries, middle eastern sars, middle eastern virus, middle east respiratory syndrome, middle east respiratory virus, middle east virus, sars like virus, sars-like virus, virus",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3914021143956545",

      "name": "EMSC_391402",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405598262000,

      "endDate": null,

      "startDate": 1405598367000,

      "keywords": null,

      "geo": "-141.20,59.46,-139.50,61.16",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3914251147289438",

      "name": "EMSC_391425",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405601594000,

      "endDate": null,

      "startDate": 1405601969000,

      "keywords": null,

      "geo": "-92.41,14.18,-91.59,15.00",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3914331152042367",

      "name": "EMSC_391433",

      "totalCount": 5,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405606347000,

      "endDate": 1405610061000,

      "startDate": 1405610608000,

      "keywords": null,

      "geo": "139.89,-4.08,140.87,-3.10",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-malaysia_airliner_crash_ukraine",

      "name": "Malaysia airliner crash Ukraine",

      "totalCount": 249020,

      "language": "en,",

      "curator": "IrinaTemnikova",

      "collectionCreationDate": 1405614435000,

      "endDate": 1405704600000,

      "startDate": 1405950615000,

      "keywords": "#MH17, Malaysia Airlines flight MH17, Malaysian Airlines Boing 777, plane crash, airplane crash, Donetsk, Донецк, Боинг-777, Боинг 777",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "GDPC",

      "name": "Disaster preparedness",

      "totalCount": 25,

      "language": "en",

      "curator": "urb_ian",

      "collectionCreationDate": 1405625448000,

      "endDate": 1406058987000,

      "startDate": 1406058990000,

      "keywords": "preparedness, disaster preparedness, resilience",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3915061202012711",

      "name": "EMSC_391506",

      "totalCount": 21120,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405656318000,

      "endDate": 1405681201000,

      "startDate": 1405656727000,

      "keywords": null,

      "geo": "26.29,37.94,26.91,38.56",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3915331220170775",

      "name": "EMSC_391533",

      "totalCount": 0,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405674476000,

      "endDate": 1405699200000,

      "startDate": 1405674733000,

      "keywords": null,

      "geo": "168.08,-16.65,169.06,-15.67",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3915371221157115",

      "name": "EMSC_391537",

      "totalCount": 55,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405675463000,

      "endDate": 1405699200000,

      "startDate": 1405675933000,

      "keywords": null,

      "geo": "-63.86,-17.88,-63.10,-17.12",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3915461226208676",

      "name": "EMSC_391546",

      "totalCount": 45,

      "language": "",

      "curator": "emscls7",

      "collectionCreationDate": 1405680514000,

      "endDate": 1405702800000,

      "startDate": 1405680735000,

      "keywords": null,

      "geo": "-71.95,-31.22,-71.23,-30.50",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3915541230235201",

      "name": "EMSC_391554",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405684541000,

      "endDate": 1405703008000,

      "startDate": 1405703307000,

      "keywords": null,

      "geo": "137.10,49.14,137.94,49.98",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-hurricane_sandy",

      "name": "Hurricane Sandy",

      "totalCount": 115,

      "language": "en",

      "curator": "andrew_ilyas",

      "collectionCreationDate": 1405685119000,

      "endDate": 1405703002000,

      "startDate": 1405703300000,

      "keywords": "#sandy, #newyork",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3915961259875997",

      "name": "EMSC_391596",

      "totalCount": 0,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405714182000,

      "endDate": null,

      "startDate": 1405714347000,

      "keywords": null,

      "geo": "32.13,29.93,32.89,30.69",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3916261282935896",

      "name": "EMSC_391626",

      "totalCount": 135,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1405737241000,

      "endDate": 1405760400000,

      "startDate": 1405737748000,

      "keywords": null,

      "geo": "42.80,38.26,43.46,38.92",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3916631300605732",

      "name": "EMSC_391663",

      "totalCount": 1475,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1405754911000,

      "endDate": 1405778400000,

      "startDate": 1405755152000,

      "keywords": null,

      "geo": "70.63,36.35,71.89,37.61",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_3916601305880769",

      "name": "EMSC_391660",

      "totalCount": 6165,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1405760186000,

      "endDate": 1405785602000,

      "startDate": 1405950641000,

      "keywords": null,

      "geo": "23.30,34.82,24.18,35.70",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-07-haiti_earthquatke_2010",

      "name": "Haiti Earthquatke 2010",

      "totalCount": 0,

      "language": "en",

      "curator": "yoshibai",

      "collectionCreationDate": 1406245673000,

      "endDate": null,

      "startDate": 1406247933000,

      "keywords": "#haiti #earthquake #2010",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-china_earthquake",

      "name": "china earthquake",

      "totalCount": 455,

      "language": "en",

      "curator": "jeff_sole",

      "collectionCreationDate": 1407162368000,

      "endDate": 1407184755000,

      "startDate": 1407163014000,

      "keywords": "#china_earthquake, #Yunnan, #Zhaotong, #China2014, #China_2014",

      "geo": "102.87,26.55,105.31,28.67",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "Philippines2013",

      "name": "Typhoon Haiyan",

      "totalCount": 8020,

      "language": "en",

      "curator": "jeff_sole",

      "collectionCreationDate": 1407163652000,

      "endDate": 1407349582000,

      "startDate": 1407184794000,

      "keywords": "#Philippines, #Philippines2013, #Philippines_2013, #typhoon_Haiyan, #Tacloban, #Yolanda",

      "geo": "124.83,11.09,125.18,11.43",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-Glenda",

      "name": "Glenda",

      "totalCount": 0,

      "language": "en",

      "curator": "liyahgarcera",

      "collectionCreationDate": 1407231535000,

      "endDate": 1407378823000,

      "startDate": 1407231820000,

      "keywords": "#glenda #GlendaPH",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-azadimarch",

      "name": "AzadiMarch",

      "totalCount": 45292820,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1407702450000,

      "endDate": 1419099027000,

      "startDate": 1418635746000,

      "keywords": "AzadiMarchPTI, PTI, #AzadiParade, #HappyIndependenceDay, #NSforPakistan, #WeWantNStoResignNow, #PakistanRejectsIK, Welcome to Islamabad, #expressnews, #InqilabMarchWithDrQadri, #RevolutionNowOrNever, #ChaloChaloIslamabadChalo, #RedZone, #weloveikbecause, #GoImranGo, #AzadiSquare, GoNawazGo, #AwamiPressure, #IslamabadMassacre, Javed Hashmi, PIMS, Corps Commanders, CORPS, #PTI4Karachi, #Lahore4PTI, Sindh",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-if_they_gunned_me_down",

      "name": "if they gunned me down",

      "totalCount": 6681375,

      "language": "en",

      "curator": "velofemme",

      "collectionCreationDate": 1408258258000,

      "endDate": 1409039862000,

      "startDate": 1408959209000,

      "keywords": "#IfTheyGunnedMeDown, #iftheygunnedmedown, iftheygunnedmedown, MikeBrown, Ferguson, FergusonShooting",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-08-rip",

      "name": "RIP",

      "totalCount": 137448635,

      "language": "en",

      "curator": "haewoon",

      "collectionCreationDate": 1408264018000,

      "endDate": 1411646084000,

      "startDate": 1411646095000,

      "keywords": "rip, r.i.p.",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_395985-208083973",

      "name": "EMSC_395985",

      "totalCount": 2955,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408541188000,

      "endDate": 1408564801000,

      "startDate": 1408541234000,

      "keywords": null,

      "geo": "47.07,31.79,48.51,33.23",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396036-187421345",

      "name": "EMSC_396036",

      "totalCount": 2420,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408561851000,

      "endDate": 1408586407000,

      "startDate": 1408562445000,

      "keywords": null,

      "geo": "47.07,32.17,47.81,32.91",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396042-182019569",

      "name": "EMSC_396042",

      "totalCount": 890,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408567253000,

      "endDate": 1408590000000,

      "startDate": 1408567853000,

      "keywords": null,

      "geo": "77.70,41.97,78.32,42.59",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396105-161744371",

      "name": "EMSC_396105",

      "totalCount": 75,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408587529000,

      "endDate": 1408611600000,

      "startDate": 1408587684000,

      "keywords": null,

      "geo": "149.88,-6.03,151.50,-4.41",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396122-153422990",

      "name": "EMSC_396122",

      "totalCount": 5220,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408595849000,

      "endDate": 1408636809000,

      "startDate": 1408611626000,

      "keywords": null,

      "geo": "-121.35,36.35,-120.69,37.01",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396169-125872598",

      "name": "EMSC_396169",

      "totalCount": 1005,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408623400000,

      "endDate": 1408647606000,

      "startDate": 1408623728000,

      "keywords": null,

      "geo": "115.74,-8.77,116.50,-8.01",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396215-95459221",

      "name": "EMSC_396215",

      "totalCount": 14845,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408653814000,

      "endDate": 1408676405000,

      "startDate": 1408654193000,

      "keywords": null,

      "geo": "27.25,35.08,28.13,35.96",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396214-95173680",

      "name": "EMSC_396214",

      "totalCount": 410,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408654099000,

      "endDate": 1408676407000,

      "startDate": 1408654193000,

      "keywords": null,

      "geo": "124.83,-8.62,125.55,-7.90",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396280-67612006",

      "name": "EMSC_396280",

      "totalCount": 3880,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408681661000,

      "endDate": 1408705202000,

      "startDate": 1408681835000,

      "keywords": null,

      "geo": "23.09,39.58,23.81,40.30",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396281-67314285",

      "name": "EMSC_396281",

      "totalCount": 3850,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408681958000,

      "endDate": 1408705202000,

      "startDate": 1408682436000,

      "keywords": null,

      "geo": "22.73,39.26,23.99,40.52",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396317-44843523",

      "name": "EMSC_396317",

      "totalCount": 2915,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1408704429000,

      "endDate": 1408730403000,

      "startDate": 1408705270000,

      "keywords": null,

      "geo": "47.31,32.17,48.07,32.93",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396322-44138217",

      "name": "EMSC_396322",

      "totalCount": 2915,

      "language": "",

      "curator": "emscls8",

      "collectionCreationDate": 1408705134000,

      "endDate": 1408730415000,

      "startDate": 1408705271000,

      "keywords": null,

      "geo": "47.27,32.22,48.15,33.10",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396382-18439042",

      "name": "EMSC_396382",

      "totalCount": 2565,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408730834000,

      "endDate": 1408860000000,

      "startDate": 1408731107000,

      "keywords": null,

      "geo": "45.34,33.52,46.08,34.26",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396404-10850203",

      "name": "EMSC_396404",

      "totalCount": 1150,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408738423000,

      "endDate": 1408860000000,

      "startDate": 1408738918000,

      "keywords": null,

      "geo": "45.43,33.31,46.27,34.15",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396639126604258",

      "name": "EMSC_396639",

      "totalCount": 12700,

      "language": "",

      "curator": "emscls10",

      "collectionCreationDate": 1408875877000,

      "endDate": 1408899600000,

      "startDate": 1408875916000,

      "keywords": null,

      "geo": "-123.08,37.45,-121.56,38.97",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "EMSC_396638126851163",

      "name": "EMSC_396638",

      "totalCount": 11150,

      "language": "",

      "curator": "emscls9",

      "collectionCreationDate": 1408876124000,

      "endDate": 1408899600000,

      "startDate": 1408876518000,

      "keywords": null,

      "geo": "-123.14,37.34,-121.52,38.96",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-gelnda",

      "name": "GlendaPH",

      "totalCount": 0,

      "language": "en",

      "curator": "rukku",

      "collectionCreationDate": 1409532753000,

      "endDate": 1409533437000,

      "startDate": 1409532795000,

      "keywords": "#glendaph",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-testing",

      "name": "testing",

      "totalCount": 5,

      "language": "en",

      "curator": "ajbangug",

      "collectionCreationDate": 1409828613000,

      "endDate": 1410004800000,

      "startDate": 1409830017000,

      "keywords": "#agostest",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-balkan_floods_september_2014",

      "name": "Balkan Floods September 2014",

      "totalCount": 115,

      "language": "en,",

      "curator": "IrinaTemnikova",

      "collectionCreationDate": 1410880540000,

      "endDate": 1411930800000,

      "startDate": 1411322458000,

      "keywords": "#Balkanfloods, Balkan flooding, Jasenovac, poplave, poplavnog talasa, #floodSERBIA, #poplave2014, #poplava,  #Kladovo, #tekija, #negotin, поплаве, поплава",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-road_accidents",

      "name": "Road Accidents",

      "totalCount": 345,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1411386589000,

      "endDate": 1411387224000,

      "startDate": 1411387217000,

      "keywords": "road accident, accident",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-ebola",

      "name": "EBOLA 2014 AFRICA",

      "totalCount": 0,

      "language": "en",

      "curator": "IamLGT",

      "collectionCreationDate": 1411457017000,

      "endDate": 1411457250000,

      "startDate": 1411457045000,

      "keywords": "#ebola #2104 #AFRICA",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-09-test",

      "name": "test321",

      "totalCount": 4145,

      "language": "en",

      "curator": "mjr216",

      "collectionCreationDate": 1412101183000,

      "endDate": 1412105958000,

      "startDate": 1412103367000,

      "keywords": "",

      "geo": "1.1,2.2,3.3,4.4",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-hajj_2014_geo",

      "name": "Hajj 2014 Geo",

      "totalCount": 145125,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1412301381000,

      "endDate": 1412679604000,

      "startDate": 1412503808000,

      "keywords": "",

      "geo": "39.7940945446,21.3988672077,39.8574336779,21.4497382666",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-hajj_2014_kw",

      "name": "Hajj 2014 KW",

      "totalCount": 126600,

      "language": "en",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1412301905000,

      "endDate": 1412679623000,

      "startDate": 1412503909000,

      "keywords": "Hajj, Hajj2014, Hajj14",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-bulgariaelectionsoctober2014",

      "name": "BulgariaElectionsOctober2014",

      "totalCount": 0,

      "language": "",

      "curator": "IrinaTemnikova",

      "collectionCreationDate": 1412374567000,

      "endDate": 1412598907000,

      "startDate": 1412503989000,

      "keywords": "#Гласувайте",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-CB2014",

      "name": "CB2014",

      "totalCount": 485,

      "language": "en,",

      "curator": "IrinaTemnikova",

      "collectionCreationDate": 1412598882000,

      "endDate": 1412859606000,

      "startDate": 1412598915000,

      "keywords": "#prayforCB204, #PrayForTLDM, #CB204, CB204",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-ebola_outbreak",

      "name": "Ebola outbreak",

      "totalCount": 892640,

      "language": ",en",

      "curator": "jeff_sole",

      "collectionCreationDate": 1413389502000,

      "endDate": 1413997200000,

      "startDate": 1413389614000,

      "keywords": "#Ebola, #Guinea, #epidemic, #outbreak",

      "geo": "-13.809814,10.271681,-12.458496,10.941192",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "HateBase",

      "name": "HateList_Next200terms",

      "totalCount": 15749505,

      "language": "en",

      "curator": "Marie_Warm",

      "collectionCreationDate": 1414086294000,

      "endDate": 1414944000000,

      "startDate": 1414337512000,

      "keywords": "boonie,\nboonies,\nmick,\nmicks,\nbluegum,\nbluegums,\nspigger,\nspiggers,\n\"border bunny\",\n\"border bunnies\",\nkike,\nkikes,\nmoulignon,\nmoulignons,\nroundeye,\nroundeyes,\nginzo,\nginzos,\nJewbacca,\nJewbaccas,\nbooner,\nbooners,\nnigre,\nnigres,\nscallie,\nscallies,\nniger,\nnigers,\ndinge,\ndinges,\nLeb,\nLebs,\nLebbo,\nLebbos,\nsambo,\nsambos,\nAfricoon,\nAfricoons,\n\"ling ling\",\n\"ling lings\",\ngub,\ngubs,\n\"banana bender\",\n\"banana benders\",\njapie,\njapies,\n\"island nigger\",\n\"island niggers\",\nhairyback,\nhairybacks,\nlugan,\nlugans,\n\"Bog Irish\",\n\"Bog Irishes\",\nblaxican,\nblaxicans,\nmoke,\nmokes,\nnigor,\nnigors,\n\"bix nood\",\n\"bix noods\",\nKushi,\nKushis,\n\"guala guala\",\n\"guala gualas\",\nhoosier,\nhoosiers,\nthicklips,\nhoosiers,\nmook,\nmooks,\nmuk,\nmuks,\n\"soup taker\",\n\"soup takers\",\nsenga,\nsengas,\nCushi,\nCushis,\npogue,\npogues,\nabo,\nabos,\n\"ping pang\",\n\"ping pangs\",\n\"proddy dog\",\n\"proddy dogs\",\n\"stump jumper\",\n\"stump jumpers\",\nhebe,\nhebes,\nmillie,\nmillies,\nboong,\nboongs,\ndago,\ndagos,\ndogun,\ndoguns,\nmocky,\nmockies,\npoppadom,\npoppadoms,\nGwat,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-hatebase_last173terms",

      "name": "HateList_Last173terms",

      "totalCount": 12590720,

      "language": "en",

      "curator": "Marie_Mall2",

      "collectionCreationDate": 1414086903000,

      "endDate": 1414695600000,

      "startDate": 1414087333000,

      "keywords": "chunky,\nchunkies,\nclam,\nclams,\nclamhead,\nclamheads,\ncocoa,\ncocoas,\n\"Cocoa Puff\",\n\"Cocoa Puffs\",\ncoconut,\ncoconuts,\ncolored,\ncoconuts,,\ncoloured,\ncoconuts,,,\ncrow,\ncrows,\ndego,\ndegos,\ndink,\ndinks,\ndogan,\ndogans,\ndomes,\ndomess,\n\"dot head\",\n\"dot heads\",\negg,\neggs,\neggplant,\neggplants,\nFairy,\nFairies,\nfez,\nfezs,\nFOB,\nFOBs,\n\"fog nigger\",\n\"fog niggers\",\nfrog,\nfrogs,\nfuzzy,\nfuzzies,\n\"fuzzy wuzzy\",\n\"fuzzy wuzzies\",\ngable,\ngables,\nGerudo,\nGerudos,\ngew,\ngews,\nghetto,\nghettos,\nghost,\nghosts,\ngin,\ngins,\ngipp,\ngipps,\n\"gook eye\",\n\"gook eyes\",\ngyppie,\ngyppies,\nheinie,\nheinies,\nho,\nhos,\nhoe,\nhoes,\nHonyak,\nHonyaks,\nHunkie,\nHunkies,\nHunky,\nHunkies,\nHunyock,\nHunyocks,\nidiot,\nidiots,\nike,\nikes,\nikey,\nikies,\niky,\nikies,\nJerry,\nJerries,\njig,\njigs,\njigarooni,\njigaroonis,\njijjiboo,\njijjiboos,\njock,\njocks,\n\"Junior Mint\",\n\"Junior Mints\",\nkotiya,\nkotiyas,\nlefty,\nlefties,\nmack,\nmacks,\nMerkin,\nMerkins,\nmickey,\nmickies,\n\"Mickey Finn\",\n\"Mickey Finns\",\nmoch,\nmoches,\nmock,\nmocks,\nmong,\nmongs,\nmonkey,\nmonkies,\nMoor,\nMo",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-10-hamburg",

      "name": "Hamburg",

      "totalCount": 1460,

      "language": "en,nl,de",

      "curator": "Rebecca1708",

      "collectionCreationDate": 1414705126000,

      "endDate": 1414792810000,

      "startDate": 1414705174000,

      "keywords": "",

      "geo": "9.79913,53.47828,10.22016,53.648241",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-11-mh370",

      "name": "MH370",

      "totalCount": 0,

      "language": "en",

      "curator": "zhangby2085",

      "collectionCreationDate": 1415002158000,

      "endDate": null,

      "startDate": 1415002198000,

      "keywords": "#MH370",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "2014-11-virgin_galactic",

      "name": "virgin galactic",

      "totalCount": 0,

      "language": "en",

      "curator": "johnypeterson",

      "collectionCreationDate": 1415003788000,

      "endDate": 1415319153000,

      "startDate": 1415318886000,

      "keywords": "#galactic, #virgingalactic, #plaincrash, #spaceship, #NTSB",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141109-0158-mimran15-test_collection_09-11",

      "name": "Test Collection 09-11",

      "totalCount": 175,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1415530744000,

      "endDate": 1415530825000,

      "startDate": 1415530756000,

      "keywords": "test",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141109-0502-ChaToX-test_of_collection",

      "name": "Test of collection",

      "totalCount": 545,

      "language": "es",

      "curator": "ChaToX",

      "collectionCreationDate": 1415541769000,

      "endDate": 1415541949000,

      "startDate": 1415541791000,

      "keywords": "#9N",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141117-1252-GOKE_LAWAL-hurricane_sandy",

      "name": "Hurricane Sandy in October 2012",

      "totalCount": 360,

      "language": "en",

      "curator": "GOKE_LAWAL",

      "collectionCreationDate": 1416243279000,

      "endDate": null,

      "startDate": 1416990671000,

      "keywords": "#sandy",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141118-0422-QTRhealth-",

      "name": "marathon",

      "totalCount": 0,

      "language": "en",

      "curator": "QTRhealth",

      "collectionCreationDate": 1416317360000,

      "endDate": null,

      "startDate": 1416317380000,

      "keywords": "@OoredooMarathon, #OoredooMarathon , #DohaMarathon ,  #QatarMarathon , doha marathon , ooredoo marathon , qatar marathon , @aspirezone , @aspirezone , #stepintohealth ,  http://marathon.ooredoo.qa/ ,  http://www.stepintohealth.qa/ , http://namat.qa/ ,  http://aspire.qa , http://www.aspirezone.qa , http://www.aspetar.com",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141126-0852-ingmarweber-singapore",

      "name": "Singapore",

      "totalCount": 1528732,

      "language": "en",

      "curator": "ingmarweber",

      "collectionCreationDate": 1417024481000,

      "endDate": 1425478235000,

      "startDate": 1425928982000,

      "keywords": "",

      "geo": "103.2783,0.8091,104.3812,1.6409",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141126-0159-jeff_sole-ebola",

      "name": "w africa ebola2014",

      "totalCount": 0,

      "language": "en",

      "curator": "jeff_sole",

      "collectionCreationDate": 1417028446000,

      "endDate": 1417028644000,

      "startDate": 1417028603000,

      "keywords": "liberia ebola",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141127-1022-HeatherLeson-thanksgiving_in_qatar",

      "name": "Thanksgiving in Qatar",

      "totalCount": 35,

      "language": "en,",

      "curator": "HeatherLeson",

      "collectionCreationDate": 1417073069000,

      "endDate": 1417248005000,

      "startDate": 1417074301000,

      "keywords": "#thanksgiving doha, #qatar thanksgiving",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141127-1027-HeatherLeson-diabetes_in_qatar",

      "name": "Diabetes in Qatar",

      "totalCount": 0,

      "language": "en",

      "curator": "HeatherLeson",

      "collectionCreationDate": 1417074051000,

      "endDate": 1417074295000,

      "startDate": 1417074223000,

      "keywords": "diabetes Qatar, health qatar",

      "geo": "50.7,24.54,51.81,26.21",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141127-0811-navinphilip-mullaperiar_dam",

      "name": "Mullaperiar Dam",

      "totalCount": 0,

      "language": "en",

      "curator": "navinphilip",

      "collectionCreationDate": 1417099316000,

      "endDate": 1417099500000,

      "startDate": 1417099344000,

      "keywords": "mullaperiar, kerala dam, idukki dam",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141201-1010-EmanuelCastaned-haiti_flood",

      "name": "Haiti Flood",

      "totalCount": 0,

      "language": "en",

      "curator": "EmanuelCastaned",

      "collectionCreationDate": 1417489868000,

      "endDate": 1417490080000,

      "startDate": 1417489894000,

      "keywords": "#haiti #flood",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141201-1013-EmanuelCastaned-",

      "name": "Morocco Flood",

      "totalCount": 0,

      "language": "en",

      "curator": "EmanuelCastaned",

      "collectionCreationDate": 1417490072000,

      "endDate": 1417490903000,

      "startDate": 1417490149000,

      "keywords": "#morocco #flood #flashflood morocco flood",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141201-1031-EmanuelCastaned-ebola",

      "name": "ebola outbreak 123",

      "totalCount": 0,

      "language": "en",

      "curator": "EmanuelCastaned",

      "collectionCreationDate": 1417491115000,

      "endDate": 1417491207000,

      "startDate": 1417491134000,

      "keywords": "ebola outbreak",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141201-1046-EmanuelCastaned-",

      "name": "Cabo Verde Fogo Volcano",

      "totalCount": 300,

      "language": "en",

      "curator": "EmanuelCastaned",

      "collectionCreationDate": 1417492147000,

      "endDate": 1417537747000,

      "startDate": 1417492167000,

      "keywords": "Cabo Verde Volano, #Cabo Verde, #volcano, Fogo Volcano",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141202-0708-RTrigwell-iraq_mission_trial",

      "name": "Iraq Mission Trial",

      "totalCount": 1480,

      "language": "en",

      "curator": "RTrigwell",

      "collectionCreationDate": 1417536522000,

      "endDate": 1417791605000,

      "startDate": 1417618058000,

      "keywords": "#Anbar, #Falluja, #Daesh",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141207-0356-_goodnewsevery1-ferguson",

      "name": "Jonah's Ferguson Analysis",

      "totalCount": 0,

      "language": "en",

      "curator": "_goodnewsevery1",

      "collectionCreationDate": 1417989434000,

      "endDate": null,

      "startDate": 1417989453000,

      "keywords": "ferguson",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141208-1201-reddysiva-hepatitis_c_india",

      "name": "hepatitis C",

      "totalCount": 5,

      "language": "en",

      "curator": "reddysiva",

      "collectionCreationDate": 1418015063000,

      "endDate": 1418015703000,

      "startDate": 1418015186000,

      "keywords": "Hepatitis, HCV, Sovaldi, ribavirin, interferon, Sovaldi India",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141214-0435-amelie_sundberg-falluja",

      "name": "Falluja Hospital",

      "totalCount": 35,

      "language": "en",

      "curator": "amelie_sundberg",

      "collectionCreationDate": 1418564372000,

      "endDate": 1418577277000,

      "startDate": 1418564459000,

      "keywords": "fallujah AND hospital",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141214-0834-enemes2000-central_africa",

      "name": "Central africa",

      "totalCount": 0,

      "language": "en,fr",

      "curator": "enemes2000",

      "collectionCreationDate": 1418607379000,

      "endDate": null,

      "startDate": 1418607399000,

      "keywords": "#centralafrica,#centrafrique",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141215-1224-SinhaKoushik-traffic_doha_collection",

      "name": "Doha Traffic Collection",

      "totalCount": 1377670,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1418635602000,

      "endDate": 1422361352000,

      "startDate": 1422361335000,

      "keywords": "bumper-to-bumper, bumper to bumper, bottleneck, jampacked, jam packed, jampacked, congestion, gridlock, hold-up, holdup, hold up, traffic jam, accident, pile-up, pile up, pileup, traffic snarl, tail back, tailback, tail-back, nose to tail, nose-to-tail, tailback, heavy traffic, traffic block, traffic queue, snailpace, snail pace, slow traffic, doha fog, doha foggy, doha dust storm, doha duststorm, doha sand storm, doha sandstorm, doha rain, traffic rerouted, traffic re-routed, road construction, terrible traffic, avoid street, massive traffic, traffic backed-up, traffic backed up, log jammed, corniche, roundabout, souq, vehicles not moving, avoid road, stuk up, stuck up, stuck-up, stuk-up",

      "geo": "51.420037,25.225689,51.621065,25.383395",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141216-1106-EAJobboard-peshawar_attack",

      "name": "Peshawar Attack - Herman Collection",

      "totalCount": 748755,

      "language": "en",

      "curator": "EAJobboard",

      "collectionCreationDate": 1418742438000,

      "endDate": 1418855591000,

      "startDate": 1418745391000,

      "keywords": "#PeshawarAttack",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141216-1108-EAJobboard-australia_siege_-_herman_collection",

      "name": "Australia Siege - Herman Collection",

      "totalCount": 25835,

      "language": "en",

      "curator": "EAJobboard",

      "collectionCreationDate": 1418742599000,

      "endDate": 1419038866000,

      "startDate": 1418855598000,

      "keywords": "#sydneysiege",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141220-0928-EAJobboard-kot",

      "name": "KOT",

      "totalCount": 90650,

      "language": "en",

      "curator": "EAJobboard",

      "collectionCreationDate": 1419039185000,

      "endDate": null,

      "startDate": 1419039206000,

      "keywords": "",

      "geo": "34.34,-4.68,41.72,4.05",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141228-1118-SinhaKoushik-air_asia_qz8501",

      "name": "Air Asia QZ8501",

      "totalCount": 0,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1419754832000,

      "endDate": 1422269713000,

      "startDate": 1422269689000,

      "keywords": "QZ8501, air asia, AirAsia, 162 passengers, z8501, 8501",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141229-0154-hermanwandabwa-airasia_loss",

      "name": "AirAsia Loss",

      "totalCount": 0,

      "language": "en",

      "curator": "hermanwandabwa",

      "collectionCreationDate": 1419832486000,

      "endDate": null,

      "startDate": 1420467260000,

      "keywords": "#QZ8501",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20141230-0339-anton_ananich-crisis_1",

      "name": "Crisis 1",

      "totalCount": 90,

      "language": "",

      "curator": "anton_ananich",

      "collectionCreationDate": 1419943330000,

      "endDate": 1419943743000,

      "startDate": 1419943748000,

      "keywords": "#minsk",

      "geo": "23.38,51.21,32.84,56.26",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150111-0739-HNTracker-testing_syria",

      "name": "Testing Syria",

      "totalCount": 44125,

      "language": "en,ar",

      "curator": "HNTracker",

      "collectionCreationDate": 1421023218000,

      "endDate": 1421146608000,

      "startDate": 1421023286000,

      "keywords": "النصرة",

      "geo": "35.4714,32.3114,42.3746,37.3185",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150113-1248-ITrojanov-c1",

      "name": "C1",

      "totalCount": 1105,

      "language": "en",

      "curator": "ITrojanov",

      "collectionCreationDate": 1421142509000,

      "endDate": 1424188811000,

      "startDate": 1424013170000,

      "keywords": "#sandy",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150115-0830-UIElection2016-us_primaries",

      "name": "US Primaries",

      "totalCount": 252465,

      "language": "en",

      "curator": "UIElection2016",

      "collectionCreationDate": 1421299887000,

      "endDate": 1421910002000,

      "startDate": 1422270029000,

      "keywords": "warren, clinton, jeb bush",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150122-1139-UIElection2016-us_primaries_22012015",

      "name": "US Primaries 22/01/2015",

      "totalCount": 0,

      "language": "en",

      "curator": "UIElection2016",

      "collectionCreationDate": 1421916124000,

      "endDate": null,

      "startDate": 1421916141000,

      "keywords": "Warren, Clinton, Jeb Bush, Bernie Sanders, Mitt Romney, Marco Rubio, Rand Paul, Chris Christie",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150127-0409-ADemarchi1985-test_27/01",

      "name": "test 27/01",

      "totalCount": 0,

      "language": "en,it",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1422371400000,

      "endDate": 1422374653000,

      "startDate": 1422371480000,

      "keywords": "#Terra dei fuochi",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150127-0503-ADemarchi1985-",

      "name": "charlie hebdo",

      "totalCount": 0,

      "language": "en,fr,it",

      "curator": "ADemarchi1985",

      "collectionCreationDate": 1422374653000,

      "endDate": null,

      "startDate": 1422374669000,

      "keywords": "#Charliehebdo",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150129-0234-SinhaKoushik-doha_traffic_new",

      "name": "Doha Traffic New",

      "totalCount": 713650,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1422532400000,

      "endDate": 1438238188000,

      "startDate": 1437200781000,

      "keywords": "bumper-to-bumper, bumper2bumper, bumper to bumper, bottleneck, jampacked, jam packed, jam-packed, congestion, gridlock, hold-up, holdup, hold up, traffic jam, accident, pile-up, pile up, pileup, traffic snarl, tail back, tailback, tail-back, nose to tail, nose-to-tail, tailback, heavy traffic, traffic block, traffic queue, snailpace, snail pace, slow traffic, doha fog, doha foggy, doha dust storm, doha duststorm, doha sand storm, doha sandstorm, doha rain, traffic rerouted, traffic re-routed, road construction, terrible traffic, avoid street, avoid road, massive traffic, traffic backed-up, traffic backed up, log jammed, roundabout, vehicles not moving, avoid road, stuk up, stuck up, stuck-up, stuk-up, dohatraffic, doha traffic, car crash, carcrash, traffic, jam, fog, sandstorm, sand storm, traffic intersection, intersection, signal, traffic signal",

      "geo": "50.9472923703,24.6951407372,51.6306566,26.163406496",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150131-0124-UIElection2016-us_primaries_20150131",

      "name": "US Primaries 2015/01/31",

      "totalCount": 8435,

      "language": "en",

      "curator": "UIElection2016",

      "collectionCreationDate": 1422699855000,

      "endDate": 1423044001000,

      "startDate": 1422867602000,

      "keywords": "Warren, Clinton, Jeb Bush, Bernie Sanders, Mitt Romney, Marco Rubio, Rand Paul, Chris Christie, Palin, Cruz, Graham, Huckabee, Jindal, Kasich, Perry, Santorum, Scott Walker",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150201-0500-realhamed-hamed_qcmc_medicine",

      "name": "Hamed_QCMC_medicine",

      "totalCount": 0,

      "language": "en",

      "curator": "realhamed",

      "collectionCreationDate": 1422799734000,

      "endDate": 1423472405000,

      "startDate": 1422866494000,

      "keywords": "Dapagliflozin , Empagliflozin , Liraglutide , Exenatide , Saxagliptin , Linagliptin , Vildagliptin , sitagliptin",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150203-0933-mimran15-geotest_west_bay_tweets",

      "name": "GeoTest West bay tweets",

      "totalCount": 0,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1422945352000,

      "endDate": 1422910800000,

      "startDate": 1422947502000,

      "keywords": "",

      "geo": "51.509859,25.311688,51.543774,25.335193",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150203-0522-SofianeAbbar-qatar_twitter",

      "name": "Qatar Twitter",

      "totalCount": 3005,

      "language": "en,,ar",

      "curator": "SofianeAbbar",

      "collectionCreationDate": 1422973341000,

      "endDate": 1423029446000,

      "startDate": 1423977632000,

      "keywords": "",

      "geo": "51.1168,24.693,51.8003,26.1959,50.7378,24.9693,51.0725,25.8236",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150204-0718-EAJobboard-kenyan_tweets",

      "name": "Kenyan Tweets",

      "totalCount": 0,

      "language": "en",

      "curator": "EAJobboard",

      "collectionCreationDate": 1423005654000,

      "endDate": null,

      "startDate": 1423636849000,

      "keywords": "",

      "geo": "33.64,-4.75,42.4,5.65",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150205-0917-UIElection2016-us_primaries_05-02-2015",

      "name": "US Primaries 05-02-2015",

      "totalCount": 94235,

      "language": "en",

      "curator": "UIElection2016",

      "collectionCreationDate": 1423117050000,

      "endDate": 1425200551000,

      "startDate": 1424793671000,

      "keywords": "Warren, Clinton, Jeb Bush, Bernie Sanders, Mitt Romney, Marco Rubio, Rand Paul, Chris Christie, Palin, Cruz, Graham, Huckabee, Jindal, Kasich, Perry, Santorum, Scott Walker",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150208-1028-jimjansen1960-",

      "name": "National Sports Day in Qatar",

      "totalCount": 1360,

      "language": "en,",

      "curator": "jimjansen1960",

      "collectionCreationDate": 1423380918000,

      "endDate": 1424002255000,

      "startDate": 1423918632000,

      "keywords": "National Sports Day in Qatar, Qatar Sport Day, Aspire Zone, Qatar Holiday, Sports Day in Aspire Zone, National Sport Day 2015",

      "geo": "50.7598,24.5107,51.6602,26.186",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150209-1249-Spaceman1111111-",

      "name": "Emirates Airline",

      "totalCount": 27910,

      "language": "en",

      "curator": "Spaceman1111111",

      "collectionCreationDate": 1423504188000,

      "endDate": 1425283200000,

      "startDate": 1424793634000,

      "keywords": "Emirates, Emirates Airlines, Emirates Airline",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150212-0921-mimran15-cricket_worldcup_2015",

      "name": "Cricket World Cup 2015",

      "totalCount": 1321395,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1423765393000,

      "endDate": 1433446761000,

      "startDate": 1433413746000,

      "keywords": "cwc15, cricket, cricket world cup, IndvsPak, PakvsInd, IndiavsPakistan",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150215-0311-jimjansen1960-qatar_total_open_2015,",

      "name": "QATAR TOTAL OPEN 2015",

      "totalCount": 17315,

      "language": "",

      "curator": "jimjansen1960",

      "collectionCreationDate": 1424002295000,

      "endDate": 1425478223000,

      "startDate": 1425478730000,

      "keywords": "QATAR TOTAL OPEN 2015, QATAR TOTAL OPEN, QATAR TOTAL, Victoria Azarenka, Simona Halep, Petra Kvitova, Agnieszka Radwanska , Caroline Wozniacki,  Angelique Kerber, Ekaterina Makarova, Venus Williams,  Sam Stosur, Svetlana Kuznetsova, Khalifa International Tennis and Squash Complex",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150216-1142-coldnose-ebola",

      "name": "Ebola krisekommunikasjon",

      "totalCount": 0,

      "language": "en",

      "curator": "coldnose",

      "collectionCreationDate": 1424083488000,

      "endDate": 1424083581000,

      "startDate": 1424083583000,

      "keywords": "#ebolaconspiracy",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150216-1033-QCRI_SC-23rd_qatar_arabian_horse",

      "name": "23rd Qatar Arabian Horse",

      "totalCount": 210,

      "language": "",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1424115262000,

      "endDate": 1425478294000,

      "startDate": 1425478731000,

      "keywords": "23rd Qatar Arabian Horse, Arabian Horse, 24th Qatar International Purebred Arabian Horse, 23th Qatar International Purebred Arabian Horse",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150216-1044-Miana909-24th_int_equestrian_sword_festival",

      "name": "24th Int Equestrian Sword Festival",

      "totalCount": 4815,

      "language": "",

      "curator": "Miana909",

      "collectionCreationDate": 1424115902000,

      "endDate": 1425478262000,

      "startDate": 1425478732000,

      "keywords": "24th International Equestrian Sword Festival, H.H The Emir Sword, Sword Festival,  Equestrian, HH The Emir 24th Intl Equestrian Sword Festival",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150218-1053-UIElection2016-us_primaries_2015-02-18",

      "name": "US Primaries 2015-02-18",

      "totalCount": 20,

      "language": "en",

      "curator": "UIElection2016",

      "collectionCreationDate": 1424246023000,

      "endDate": 1424246050000,

      "startDate": 1424525319000,

      "keywords": "Warren, Clinton, Jeb Bush, Bernie Sanders, Mitt Romney, Marco Rubio, Rand Paul, Chris Christie, Palin, Cruz, Graham, Huckabee, Jindal, Kasich, Perry, Santorum, Scott Walker",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150222-0939-mimran15-",

      "name": "You understand the data you collect will be made available for",

      "totalCount": 5,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1424587183000,

      "endDate": 1424587192000,

      "startDate": 1424587192000,

      "keywords": "test",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150222-0240-haewoon-new_york_times",

      "name": "Evergreen content",

      "totalCount": 12455,

      "language": "",

      "curator": "haewoon",

      "collectionCreationDate": 1424605279000,

      "endDate": 1427630410000,

      "startDate": 1427025398000,

      "keywords": "nyti.ms, nytimes.com, aljazeera.com",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150223-0204-PeterMosur-dar_es_salaam_(sw)",

      "name": "Dar es Salaam (sw)",

      "totalCount": 0,

      "language": "sw",

      "curator": "PeterMosur",

      "collectionCreationDate": 1424718547000,

      "endDate": 1425658297000,

      "startDate": 1425658303000,

      "keywords": "dar es salaam mafuriko, mafuriko dar es salaam, mafuriko ya dar es salaam, mafuriko barabara ya dar es salaam, mafuriko",

      "geo": "39.186172,-6.882796,39.328872,-6.722768",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150225-0355-mimran15-test_new",

      "name": "Test_new",

      "totalCount": 4265,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1424868948000,

      "endDate": 1434369969000,

      "startDate": 1434363755000,

      "keywords": "pakistan, -uk",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150228-1157-SinhaKoushik-budget_india",

      "name": "Budget India",

      "totalCount": 184205,

      "language": "",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1425113874000,

      "endDate": 1425208833000,

      "startDate": 1425116935000,

      "keywords": "#SuperBudget, India budget, modi budget, budget",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150301-1201-UIElection2016-us_primaries_01-03-2015",

      "name": "US Primaries 01-03-2015",

      "totalCount": 588925,

      "language": "en",

      "curator": "UIElection2016",

      "collectionCreationDate": 1425200551000,

      "endDate": 1425478244000,

      "startDate": 1425928902000,

      "keywords": "Warren, Clinton, Jeb Bush, Bernie Sanders, Mitt Romney, Marco Rubio, Rand Paul, Chris Christie, Palin, Cruz, Graham, Huckabee, Jindal, Kasich, Perry, Santorum, Scott Walker",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150303-0312-TauTechnology-tech",

      "name": "Tech",

      "totalCount": 0,

      "language": "en",

      "curator": "TauTechnology",

      "collectionCreationDate": 1425388387000,

      "endDate": 1425388758000,

      "startDate": 1425478752000,

      "keywords": "#tech, #techafricam, #ict4d",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "20150304-0828-cant_b_blank-",

      "name": "Queenstown earthquake",

      "totalCount": 0,

      "language": "en",

      "curator": "cant_b_blank",

      "collectionCreationDate": 1425418348000,

      "endDate": 1425420434000,

      "startDate": 1425479333000,

      "keywords": "#eqnz , #quakenz, #nzquake",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150304094652_overfishing_crisis",

      "name": "Overfishing crisis",

      "totalCount": 0,

      "language": "en",

      "curator": "TrevorABranch",

      "collectionCreationDate": 1425491253000,

      "endDate": 1425491549000,

      "startDate": 1425491552000,

      "keywords": "fisheries",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150306093256_",

      "name": "demonstration",

      "totalCount": 0,

      "language": "en",

      "curator": "NoelDickover",

      "collectionCreationDate": 1425652555000,

      "endDate": null,

      "startDate": 1425652583000,

      "keywords": "#police #baton #attack #peaceful #protest",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150311110558_test-collection",

      "name": "Test-Collection",

      "totalCount": 23700,

      "language": "en",

      "curator": "test_socialq",

      "collectionCreationDate": 1426061201000,

      "endDate": 1426604523000,

      "startDate": 1426608671000,

      "keywords": "Qatar traffic",

      "geo": "5.9559,45.818,10.4921,47.8084",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150314105656_cyclonepambounding",

      "name": "CyclonePamBounding",

      "totalCount": 3230,

      "language": "",

      "curator": "justineqcri",

      "collectionCreationDate": 1426330648000,

      "endDate": 1426939202000,

      "startDate": 1426763584000,

      "keywords": "cyclone pam, vanuatu",

      "geo": "161.9275,-23.6349,171.6227,-11.5268",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150315194330_test_aidr_olen",

      "name": "test aidr Olen",

      "totalCount": 0,

      "language": "nl",

      "curator": "mozzemozze",

      "collectionCreationDate": 1426445084000,

      "endDate": 1426445146000,

      "startDate": 1426445114000,

      "keywords": "#oleninbeeld",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150319000143_bardo_attack",

      "name": "Bardo Attack",

      "totalCount": 3755,

      "language": "en",

      "curator": "hermanwandabwa",

      "collectionCreationDate": 1426694619000,

      "endDate": 1427371200000,

      "startDate": 1427025478000,

      "keywords": "#Bardo",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150319101419_",

      "name": "aljazeera-urls",

      "totalCount": 62545,

      "language": "en",

      "curator": "b492004",

      "collectionCreationDate": 1426749909000,

      "endDate": 1427965204000,

      "startDate": 1427358753000,

      "keywords": "aljazeera, www.aljazeera.com, aljazeera.com, www.aljazeera.co, aljazeera.co",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150325140028_hurrican",

      "name": "Hurrican",

      "totalCount": 0,

      "language": "en",

      "curator": "katyprogrammer",

      "collectionCreationDate": 1427263274000,

      "endDate": 1427266258000,

      "startDate": 1427263310000,

      "keywords": "#earthquake #Taiwan",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150325194705_earthquake",

      "name": "earthquake in new york",

      "totalCount": 0,

      "language": "en",

      "curator": "nihaoalison",

      "collectionCreationDate": 1427327362000,

      "endDate": 1427395762000,

      "startDate": 1427327501000,

      "keywords": "earthquake new york",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150327093554_hurricane_sandy",

      "name": "Hurricane Sandy in 2015",

      "totalCount": 335,

      "language": "en",

      "curator": "katyprogrammer",

      "collectionCreationDate": 1427420220000,

      "endDate": 1427594409000,

      "startDate": 1427420241000,

      "keywords": "#Sandy",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150328202750_canterbury_earthquakes_and_recovery",

      "name": "Canterbury Earthquakes and Recovery",

      "totalCount": 255,

      "language": "en",

      "curator": "zitazether",

      "collectionCreationDate": 1427527722000,

      "endDate": 1429358400000,

      "startDate": 1429183249000,

      "keywords": "#eqnz, #Christchurch, Christchurch rebuild, Christchurch recovery",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150328075159_concern_worldwide",

      "name": "Concern Worldwide",

      "totalCount": 0,

      "language": "en",

      "curator": "lampofo",

      "collectionCreationDate": 1427529163000,

      "endDate": 1427702441000,

      "startDate": 1427529183000,

      "keywords": "concern worldeide",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150330080015_kasra",

      "name": "Kasra",

      "totalCount": 0,

      "language": "",

      "curator": "SofianeAbbar",

      "collectionCreationDate": 1427691662000,

      "endDate": 1427662800000,

      "startDate": 1427692878000,

      "keywords": "kasra.co, kasra",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150401174102_",

      "name": "plane crash",

      "totalCount": 146210,

      "language": "en",

      "curator": "nabil_izyan",

      "collectionCreationDate": 1427899307000,

      "endDate": 1429185600000,

      "startDate": 1427899327000,

      "keywords": "newyork",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150402095305_",

      "name": "Typhoon Maysak",

      "totalCount": 17145,

      "language": ",en,tl",

      "curator": "Fidget02",

      "collectionCreationDate": 1427965064000,

      "endDate": 1429195884000,

      "startDate": 1429182585000,

      "keywords": "Typhoon Maysak, Cabagan, #ReliefPH, #RescuePH, #SafeNow, #FloodPH, #TracingPH,",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150403090823_garissa_attack",

      "name": "Garissa Attack",

      "totalCount": 194700,

      "language": "en",

      "curator": "EAJobboard",

      "collectionCreationDate": 1428023340000,

      "endDate": 1429358400000,

      "startDate": 1429182619000,

      "keywords": "#GarissaAttack",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150407084132_syria",

      "name": "Syria transaction",

      "totalCount": 5545,

      "language": "ar,en",

      "curator": "sbmalkawi",

      "collectionCreationDate": 1428385456000,

      "endDate": 1430758800000,

      "startDate": 1430150826000,

      "keywords": "remittance",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150416215336_test-totrack",

      "name": "Test-toTrack",

      "totalCount": 2975,

      "language": "en",

      "curator": "SinhaKoushik",

      "collectionCreationDate": 1429210628000,

      "endDate": 1429693590000,

      "startDate": 1429211394000,

      "keywords": "ipl, cricket, news, birthday, happy, traffic, internet, net, gandhi, rahul gandhi, modi, football, you",

      "geo": "67.6771025658,5.4930069069,93.3729095459,39.5153917496",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150429090702_geotagged_syria",

      "name": "Geotagged Syria",

      "totalCount": 68950,

      "language": "en",

      "curator": "ingmarweber",

      "collectionCreationDate": 1430287837000,

      "endDate": 1430906824000,

      "startDate": 1430287858000,

      "keywords": "",

      "geo": "35.22,28.84,48.72,37.42",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150502115112_",

      "name": "Jesuischarlie",

      "totalCount": 36135,

      "language": "",

      "curator": "JisunAn",

      "collectionCreationDate": 1430556914000,

      "endDate": 1430906831000,

      "startDate": 1430560404000,

      "keywords": "#CharlieHebdo, #JeSuisAhmed, #JeSuisCharlie, #JeSuisJuif, #JeNeSuisPasCharlie, #JeSuisPasCharlie, #Hebdo",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150502120055_charlie_hebdo_gets_pen_award",

      "name": "02-05-2015 Charlie Hebdo",

      "totalCount": 231040,

      "language": "",

      "curator": "yelenamm",

      "collectionCreationDate": 1430557463000,

      "endDate": 1431013525000,

      "startDate": 1430557548000,

      "keywords": "hebdo, charliehebdo, jesuischarlie, JeSuisAhmed, JeNeSuisPasCharlie, JeSuisPasCharlie",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150503171508_2015_05_03_qatar",

      "name": "2015 05 03 Qatar",

      "totalCount": 527485,

      "language": "",

      "curator": "qscspider",

      "collectionCreationDate": 1430662527000,

      "endDate": 1431013526000,

      "startDate": 1430663319000,

      "keywords": "Cátar, Catar, Katar, Katara, Kataras, Katari, Kataro, Qadar, Qatar, قطر, कतर, ਕਤਰ, 卡塔尔, 卡塔爾, 카타르, קטאר, कतार, કતાર, కతర్, ກາຕາ, カタール, Κατάρ, Катар, Қатар, Կատար, কাতার, ಕತಾರ್, ഖത്തർ, කටාර්, กาตาร์, קאַטאַר, கத்தார், ប្រទេសកាតា, ကာတာနိုင်ငံ",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150504144559_chileearth",

      "name": "ChileEarthquake",

      "totalCount": 0,

      "language": "",

      "curator": "JulienLouton",

      "collectionCreationDate": 1430743671000,

      "endDate": 1430743764000,

      "startDate": 1430743691000,

      "keywords": "ChileEarthquake",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150509120035_2015_05_09_qatar",

      "name": "2015 05 09 Qatar",

      "totalCount": 100700,

      "language": "",

      "curator": "qscspider",

      "collectionCreationDate": 1431162120000,

      "endDate": 1431265147000,

      "startDate": 1431162143000,

      "keywords": "Cátar, Catar, Katar, Katara, Kataras, Katari, Kataro, Qadar, Qatar, قطر, कतर, ਕਤਰ, 卡塔尔, 卡塔爾, 카타르, קטאר, कतार, કતાર, కతర్, ກາຕາ, カタール, Κατάρ, Катар, Қатар, Կատար, কাতার, ಕತಾರ್, ഖത്തർ, කටාර්, กาตาร์, קאַטאַר, கத்தார், ប្រទេសកាតា, ကာတာနိုင်ငံ",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150509121132_2015_05_09_charlie_hebdo",

      "name": "2015 05 09 Charlie Hebdo",

      "totalCount": 16860,

      "language": "",

      "curator": "yelenamm",

      "collectionCreationDate": 1431162781000,

      "endDate": 1431290223000,

      "startDate": 1431162806000,

      "keywords": "hebdo, charliehebdo, jesuischarlie, JeSuisAhmed, JeNeSuisPasCharlie, JeSuisPasCharlie",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150510151834_test_run",

      "name": "test run",

      "totalCount": 250,

      "language": "en,",

      "curator": "TStMach",

      "collectionCreationDate": 1431260502000,

      "endDate": null,

      "startDate": 1431260524000,

      "keywords": "#nepalearthquake, nepal earthquake",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150510174500_2015_05_10_qatar",

      "name": "2015 05 10 Qatar",

      "totalCount": 182620,

      "language": "",

      "curator": "qscspider",

      "collectionCreationDate": 1431269123000,

      "endDate": 1431460491000,

      "startDate": 1431269142000,

      "keywords": "Cátar, Catar, Katar, Katara, Kataras, Katari, Kataro, Qadar, Qatar, قطر, कतर, ਕਤਰ, 卡塔尔, 卡塔爾, 카타르, קטאר, कतार, કતાર, కతర్, ກາຕາ, カタール, Κατάρ, Катар, Қатар, Կատար, কাতার, ಕತಾರ್, ഖത്തർ, කටාර්, กาตาร์, קאַטאַר, கத்தார், ប្រទេសកាតា, ကာတာနိုင်ငံ",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150511163844_2015_09_11_charlie_hebdo",

      "name": "2015 09 11 Charlie Hebdo",

      "totalCount": 16230,

      "language": "",

      "curator": "yelenamm",

      "collectionCreationDate": 1431351557000,

      "endDate": 1431458632000,

      "startDate": 1431351577000,

      "keywords": "hebdo, charliehebdo, jesuischarlie, JeSuisAhmed, JeNeSuisPasCharlie, JeSuisPasCharlie",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150513161041_withings_quantified_self",

      "name": "Withings Quantified Self",

      "totalCount": 81835,

      "language": "en",

      "curator": "ingmarweber",

      "collectionCreationDate": 1431522665000,

      "endDate": 1431936563000,

      "startDate": 1431682709000,

      "keywords": "my weight",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150515131839_amtrak_188",

      "name": "Amtrak 188",

      "totalCount": 18130,

      "language": "en",

      "curator": "DexterDogGr8",

      "collectionCreationDate": 1431710352000,

      "endDate": 1432097265000,

      "startDate": 1431710372000,

      "keywords": "#Amtrak188, #AmtrakDerailment",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150516164532_sports",

      "name": "Sports",

      "totalCount": 1380,

      "language": "en",

      "curator": "Tatiana_Online",

      "collectionCreationDate": 1431784042000,

      "endDate": 1431784475000,

      "startDate": 1431784065000,

      "keywords": "sports,sport,boxing,f1",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150522134652_nepal_earthquake",

      "name": "Nepal earthquake www2015",

      "totalCount": 83460,

      "language": "en",

      "curator": "marko_grobelnik",

      "collectionCreationDate": 1432295333000,

      "endDate": 1432328504000,

      "startDate": 1432295340000,

      "keywords": "nepal, earthquake",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150525233152_nepal_earthquake",

      "name": "Nepal Earthquake 2014",

      "totalCount": 0,

      "language": null,

      "curator": "sohailyuva",

      "collectionCreationDate": 1432577049000,

      "endDate": 1434560648000,

      "startDate": 1434560500000,

      "keywords": null,

      "geo": null,

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150525204932_test",

      "name": "Test Collection 1",

      "totalCount": 130,

      "language": "en",

      "curator": "EmreKIRACc",

      "collectionCreationDate": 1432605013000,

      "endDate": 1432605209000,

      "startDate": 1432605036000,

      "keywords": "#help, #flood, disaster, relief",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150526111306_flooding",

      "name": "FLOODING",

      "totalCount": 248270,

      "language": "en",

      "curator": "EmreKIRACc",

      "collectionCreationDate": 1432656942000,

      "endDate": 1432674708000,

      "startDate": 1432674208000,

      "keywords": "#flood, #houston, flood houston, relief, help, #austin, flood austin, flooding",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150527121519_india_heatwave",

      "name": "India Heatwave",

      "totalCount": 4715,

      "language": "en,ur",

      "curator": "Fidget02",

      "collectionCreationDate": 1432725661000,

      "endDate": 1432742279000,

      "startDate": 1432729938000,

      "keywords": "india Water, Andhra Pradesh, Telangana,",

      "geo": "68.16,6.75,97.4,35.51",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150528120351_icwsm",

      "name": "ICWSM",

      "totalCount": 1470,

      "language": "en",

      "curator": "ingmarweber",

      "collectionCreationDate": 1432811071000,

      "endDate": 1433260536000,

      "startDate": 1432885600000,

      "keywords": "#icwsm, #icwsm15 #icwsm2015",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150531172701_",

      "name": "Polarized Twitter Collection",

      "totalCount": 3910,

      "language": "en",

      "curator": "yelenamm",

      "collectionCreationDate": 1433082945000,

      "endDate": 1433151279000,

      "startDate": 1433146809000,

      "keywords": "tcot, tlot, ocra, gop, republicans, teaparty, right, sgp, hhrs, secede, secession, secedefromunion, democrats, p2, topprog, progressives, p3, p2ba",

      "geo": "-128.17,24.51,-58.41,49.49",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150601133702_test",

      "name": "test_trash",

      "totalCount": 0,

      "language": "en",

      "curator": "muskanalways",

      "collectionCreationDate": 1433155116000,

      "endDate": 1433155188000,

      "startDate": 1433155169000,

      "keywords": "Kargil",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150602105759_burundi",

      "name": "Burundi",

      "totalCount": 200,

      "language": "en,",

      "curator": "BenParker140",

      "collectionCreationDate": 1433239124000,

      "endDate": 1433446766000,

      "startDate": 1434536578000,

      "keywords": "Imbonerakure, abatwip",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150622124649_katara_ramadan_event",

      "name": "Katara Ramadan Event",

      "totalCount": 2890,

      "language": "en",

      "curator": "mimran15",

      "collectionCreationDate": 1434966451000,

      "endDate": 1437480438000,

      "startDate": 1437111621000,

      "keywords": "Katara launch Ramadan Festival, Katara Ramadan, Ramadan Festival, Katara Ramadan Festival",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150622191734_mers_sk",

      "name": "MERS SK",

      "totalCount": 0,

      "language": "en",

      "curator": "johnboazlee",

      "collectionCreationDate": 1434971337000,

      "endDate": null,

      "startDate": 1434971359000,

      "keywords": "#MERS #southkorea #SK #SARS",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150623160118_world_cup_2022",

      "name": "World Cup 2022",

      "totalCount": 495,

      "language": "en",

      "curator": "ibroso1tan",

      "collectionCreationDate": 1435064828000,

      "endDate": 1435072051000,

      "startDate": 1435064953000,

      "keywords": "#fifa,#worldcup2022",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150629011217_earthquake",

      "name": "earthquake nepal",

      "totalCount": 2195,

      "language": "en",

      "curator": "Calinos",

      "collectionCreationDate": 1435533205000,

      "endDate": 1435762800000,

      "startDate": 1435586903000,

      "keywords": "earthquake nepal",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150629122739_nepal_earthquake",

      "name": "Philippine Earthquake",

      "totalCount": 0,

      "language": "en",

      "curator": "johnboazlee",

      "collectionCreationDate": 1435551556000,

      "endDate": 1435551583000,

      "startDate": 1435551578000,

      "keywords": "#lindol #earthquake #wsf",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150701232400_ukraine",

      "name": "Ukraine crisis",

      "totalCount": 0,

      "language": "en",

      "curator": "Buddhuza",

      "collectionCreationDate": 1435785939000,

      "endDate": 1435786032000,

      "startDate": 1435785959000,

      "keywords": "ukraine kiev kyiv donetsk donbas luhansk lugansk",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150708180831_quakemap",

      "name": "quakemap",

      "totalCount": 0,

      "language": "en",

      "curator": "Stha_Megha",

      "collectionCreationDate": 1436358119000,

      "endDate": 1436358523000,

      "startDate": 1436358144000,

      "keywords": "quakemap, kll",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150710192735_gfbfgbfg",

      "name": "lalitGate",

      "totalCount": 560,

      "language": "en",

      "curator": "GoyalKushalKant",

      "collectionCreationDate": 1436536687000,

      "endDate": 1436539261000,

      "startDate": 1436536708000,

      "keywords": "#india",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150712121506_dubai",

      "name": "Dubai",

      "totalCount": 1570,

      "language": "en",

      "curator": "qcri_qatar",

      "collectionCreationDate": 1436692598000,

      "endDate": 1438689607000,

      "startDate": 1438084374000,

      "keywords": "Eid Fireworks, Eid Fireworks 2015, Eid Fireworks dubai, Eid Firework, Eid Firework 2015, Eid Firework dubai, Eid Al Fitr, Eid Al Fitr 2015, Eid Al Fitr dubai, Layali Dubai, Layali Dubai 2015, The Laughter Factory, Laughter Factory",

      "geo": "54.895991,24.79141,55.563698,25.356306",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150712122456_qatar",

      "name": "Qatar",

      "totalCount": 1500,

      "language": "en",

      "curator": "jswong65",

      "collectionCreationDate": 1436693162000,

      "endDate": 1438488018000,

      "startDate": 1437880265000,

      "keywords": "Eid Al Fitr, Eid Al Fitr 2015, Eid Al Fitr Festival 2015, Eid Al Fitr Qatar, Eid Al Fitr 2015 Qatar, Eid Al Fitr Festival 2015 Qatar, Elmo Makes Music, Elmo Make Music, Sesame Street Live",

      "geo": "50.7501,24.4711,51.6433,26.1831",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150712125001_abu_dahbi",

      "name": "Abu Dhabi",

      "totalCount": 30,

      "language": "en",

      "curator": "JSW7565",

      "collectionCreationDate": 1436694783000,

      "endDate": 1438689614000,

      "startDate": 1438084629000,

      "keywords": "Mohamed Abdo Live, Mohamed Abdo Live, Mohamed Abdo 2015, Mohamed Abdo Live 2015, Habib Elyasi, Habib Elyasi Live, Habib Elyasi 2015, Habib Elyasi Live 2015 Barney Live World Tour, Barney Live, Barney World Tour, Barney Live World Tour 2015, Barney Live 2015, Barney World Tour 2015, Barney Live abu dhabi, Barney World Tour abu dhabi",

      "geo": "54.268856,24.151766,54.85096,24.62133",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150720145636_idp_villavo",

      "name": "IDP Villavo",

      "totalCount": 0,

      "language": "en,es",

      "curator": "Ascapa01",

      "collectionCreationDate": 1437389893000,

      "endDate": 1437394978000,

      "startDate": 1437389984000,

      "keywords": "#mataron,#sacaron,#Villavo,#capital,#tierra,#uniformados",

      "geo": "-74.9028,2.6277,-71.0699,4.3157",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150720162521_idp_meta",

      "name": "IDP Meta",

      "totalCount": 0,

      "language": null,

      "curator": "Ascapa01",

      "collectionCreationDate": 1437395140000,

      "endDate": 1437395236000,

      "startDate": 1437395164000,

      "keywords": null,

      "geo": null,

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150721180732_ft_july_2015",

      "name": "FT July 2015",

      "totalCount": 4875,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1437480510000,

      "endDate": 1438754407000,

      "startDate": 1438580227000,

      "keywords": "Lunch with the FT: Sheikha Moza, Sheikha Moza, SheikhaMoza, Sheikha Moza interview, Financial Times Interviews Sheikha Moza, Sheikha Moza Qatar, Sheikha Moza Doha, Sheikha, Sheikha Moza Interview, Qatar Foundation, Moza bint Nasser al-Miss­ned, Education City, Qatar Education City",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150721194511_test_july_2015",

      "name": "Test July 2015",

      "totalCount": 165,

      "language": "",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1437486338000,

      "endDate": 1437486352000,

      "startDate": 1437486344000,

      "keywords": "uk",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150726123146_kek",

      "name": "kek",

      "totalCount": 233420,

      "language": "en",

      "curator": "MiakoMoto",

      "collectionCreationDate": 1437917563000,

      "endDate": 1438257601000,

      "startDate": 1438084452000,

      "keywords": "nigger, faggot, kike, jew, 4chan, 8chan, op",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150727224910_geo_collection",

      "name": "Geo Collection",

      "totalCount": 1542530,

      "language": "",

      "curator": "QCRI_SC",

      "collectionCreationDate": 1438015852000,

      "endDate": 1442070020000,

      "startDate": 1441957862000,

      "keywords": "",

      "geo": "50.7501,24.4711,51.6433,26.1831, 52.4535,22.9504,56.9199,26.4644, 58.228376,23.521122,58.618957,23.645569, 50.378151,25.556458,50.824846,26.330393",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150803163902_fitness",

      "name": "Fitness",

      "totalCount": 1606845,

      "language": "",

      "curator": "ingmarweber",

      "collectionCreationDate": 1438598562000,

      "endDate": 1442602800000,

      "startDate": 1441390806000,

      "keywords": "#nikeplus, #endomondo, #fitbit, #myfitnesspal, #runtastic, withings, runkeeper, jawbone, nikeplus, fitbit, endomondo",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150812144228_",

      "name": "Alagamento RS",

      "totalCount": 0,

      "language": "pt",

      "curator": "leticiaverona",

      "collectionCreationDate": 1439391707000,

      "endDate": 1439392171000,

      "startDate": 1439392022000,

      "keywords": "chuvas rs, desabrigados rs, alagamento rs",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150826140849_hurricane_sandy",

      "name": "nepalquake",

      "totalCount": 65,

      "language": "en",

      "curator": "hsjomaa",

      "collectionCreationDate": 1440576624000,

      "endDate": 1440577011000,

      "startDate": 1440576636000,

      "keywords": "nepal, earthquake",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150901094822_u-report_zambia",

      "name": "U-Report Zambia Test",

      "totalCount": 0,

      "language": null,

      "curator": "jas_pow",

      "collectionCreationDate": 1441079549000,

      "endDate": 1441252800000,

      "startDate": 1441079570000,

      "keywords": null,

      "geo": null,

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150907182959_refugees",

      "name": "Refugees",

      "totalCount": 3178920,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1441629131000,

      "endDate": 1444708800000,

      "startDate": 1444102136000,

      "keywords": "#syrianrefugeesgr, refugees, #refugeeswelcome",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150909141523_suffolk_marathon_9-9-2015",

      "name": "Suffolk Marathon 9-9-2015",

      "totalCount": 0,

      "language": "en",

      "curator": "fresfr27",

      "collectionCreationDate": 1441811867000,

      "endDate": 1441814012000,

      "startDate": 1441813195000,

      "keywords": "Suffolk Marathon, SC Marathon, Suffolk County Marathon, Suffolk runners, SC Marathon Runners, SuffokMarathon,Suffolk County Marathon,",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150910111718_sandy",

      "name": "Sandy",

      "totalCount": 0,

      "language": "en",

      "curator": "DataLook",

      "collectionCreationDate": 1441865899000,

      "endDate": 1441866023000,

      "startDate": 1441865920000,

      "keywords": "#sfearthquake",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150911113025_suffolk_county_marathon_geo",

      "name": "Suffolk County Marathon Geo",

      "totalCount": 0,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1441974960000,

      "endDate": null,

      "startDate": null,

      "keywords": "",

      "geo": "-73.211226,40.699432,-72.951859,40.779609",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150911125911_suffolk_county_marathon_geographic",

      "name": "Suffolk County Marathon Geographic",

      "totalCount": 535,

      "language": "en",

      "curator": "Spaceman1111111",

      "collectionCreationDate": 1441979995000,

      "endDate": 1442242011000,

      "startDate": 1441980045000,

      "keywords": "",

      "geo": "-73.211226,40.699432,-72.951859,40.779609",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150915212423_bitcoin_turkce_tweetler",

      "name": "Bitcoin Turkce Tweetler",

      "totalCount": 510,

      "language": "tr",

      "curator": "Reddit_Privacy",

      "collectionCreationDate": 1442330842000,

      "endDate": 1442440238000,

      "startDate": 1442330869000,

      "keywords": "#bitcoin, #litecoin, #cryptocurrent, #kriptopara, #sanalpara, #dashcoin",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150916171617_chile_earthquake_sept_16_2015",

      "name": "Chile Earthquake Sept 16 2015",

      "totalCount": 0,

      "language": "en,es",

      "curator": "JoyceMonsees",

      "collectionCreationDate": 1442438573000,

      "endDate": null,

      "startDate": null,

      "keywords": "earthquake, #Chile, terremoto, #seismo, #tsunami, Hawaii tsunami, tsunami evacuation, evacuating, #ChileQuake, #Valparaiso, #Illapel,",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150916173608_chile_earthquake_sept_16_2015",

      "name": "Chile Earthquake and Tsunami Sept 16 2015",

      "totalCount": 56135,

      "language": "en",

      "curator": "JoyceAIDR",

      "collectionCreationDate": 1442439655000,

      "endDate": 1442613606000,

      "startDate": 1442439675000,

      "keywords": "#Chile, #ChileEarthquake,  #earthquake, #terremoto, #ChileQuake, #antofagasta,  #Concón,  #Valparaiso,  #Illapel",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150922032846_",

      "name": "name rotaib",

      "totalCount": 0,

      "language": null,

      "curator": "13v11",

      "collectionCreationDate": 1442871236000,

      "endDate": 1443247200000,

      "startDate": 1443072979000,

      "keywords": null,

      "geo": null,

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150925110048_hajj_stampede",

      "name": "Hajj Stampede",

      "totalCount": 371205,

      "language": "en,ar",

      "curator": "jlucasQatar",

      "collectionCreationDate": 1443157599000,

      "endDate": 1445929080000,

      "startDate": 1445928649000,

      "keywords": "Hajj, pilgrimage victim, Hajj victim, pilgrimage, Hajjpilgrimage, HajjStampede, thedrum, mina, Muzdalifa, Arafat , Hajj2015, Makkah, Mecca, Haj, Hajj pilgromage, Hajj Stampede, pilgrimage stampede, Mecca stampede, Jamarat, Jamarat Bridge, Street 223, Street 204, 2019s exits, جسر الجمرات,\nالحج بجروح,\nموسم الحج,\nخائفة حاج,\nالحج الإندفاع,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150925104635_hajj_geo",

      "name": "Hajj Geo",

      "totalCount": 6885,

      "language": "en,ar",

      "curator": "Fidget02",

      "collectionCreationDate": 1443163703000,

      "endDate": 1445250293000,

      "startDate": 1445055506000,

      "keywords": "جسر الجمرات,\nالحج بجروح,\nموسم الحج,\nخائفة حاج,\nالحج الإندفاع,",

      "geo": "39.4334,21.1971,40.6346,21.7206",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "150926122950_",

      "name": "Earthquake Japan",

      "totalCount": 0,

      "language": "en",

      "curator": "MrPlamenHristov",

      "collectionCreationDate": 1443256275000,

      "endDate": 1443430800000,

      "startDate": 1443256296000,

      "keywords": "#earthquakesf",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151002151319_hurrican_joaquim",

      "name": "Hurrican Joaquin",

      "totalCount": 340,

      "language": "en",

      "curator": "PeterMosur",

      "collectionCreationDate": 1443802589000,

      "endDate": 1444018654000,

      "startDate": 1443889929000,

      "keywords": "joaquin, hurricane joaquin, Storm, Emergency, Road Closure, erosion, Outage, Closure, Flooding, Accident, Fire, ambulance, Injury, Evacuate, Death, Killed, Hospital, Suffolk EMS, Hospital, Flood, flooding, overwash, high tide, heavy waves, waves",

      "geo": "-73.303527832,40.5597213468,-71.628112793,41.2550973019",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151003132915_hurrican_joaquin__non_geographic",

      "name": "Hurrican Joaquin  non geographic",

      "totalCount": 101485,

      "language": "en",

      "curator": "fresfr27",

      "collectionCreationDate": 1443883050000,

      "endDate": 1444129842000,

      "startDate": 1444102586000,

      "keywords": "Suffolk Hurricane, Brookhaven, Suffolk Outage, Suffolk Closure, Suffolk Flooding, Suffolk Accident, \nSuffolk Fire,  South Shore Flooding, L.I.  Flooding. Joaquin L.I. Joaquin Suffolk, Suffolk Ems,Suffolk Ambulance\nSuffolk Injury, Suffolk Evacuate, Suffolk Advisory,\nSuffolk Co Hurricane, Ocean Beach, Davis Park, FI Pines, Fire Island, FI Ferry, FI Damage, FI Beach Erosion, FI waves, Moriches Flooding, Seaview, Saltaire, Kismet, Cherry Grove, Watch Hill, Smith Point Park Moriches Inlet, Point O'Woods, Flood, high tide,\nJoaquin Suffolk, Suffolk Co Storm, Suffolk Submerged, Brookhaven Flooding, Islip Flooding, Fire Island Erosion, Fire Island Damage, Fire Island Beach, Riverhead Flooding, Southold Flooding, East Hampton Flooding, Westhampton Flooding, Southampton Flooding, Babylon Flooding, Huntington Flooding, Smithtown Flooding, Brookhaven Flooding, Mastic Flooding, Shirley Flooding, Lindenhurst Flooding, \nSuffolk Tide, High Tide Suffolk, Flooding Shelter Island, Overwash, Wash Over",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151006100951_monitoring_suffolk_ny_for_emergency's",

      "name": "Monitoring Suffolk NY for Emergency's",

      "totalCount": 233335,

      "language": "en",

      "curator": "fresfr27",

      "collectionCreationDate": 1444129842000,

      "endDate": 1444312800000,

      "startDate": 1444139546000,

      "keywords": "accident,\nambulance,\nbleeding,\nbleeding,\nbomb,\nbreathing,\ndead,\ndeath,\nexplosion,\nfaint,\nfall,\nfight,\nfire,\nheart attack,\nheat,\nhurt,\ninjury,\nover heat,\npassed out,\npoison,\nshooting,\nsick,\nstabbing,\nsuspicous package,\nunattended,\nstroke,\nterror,\nterrorism,\nweapon,\nhazard,\nhazardous,\nbackpack,\nmob,\nangry,\nplanted,\nhide,\nhidden,\nsabotage,\nplot,\nconspiracy,\ntarget,\nground zero,\nprotest,\nchemical,\ncontaminate,\ninfectious,\nal qaeda,\nisis,\ndomestic,\ncriminal,\nassault,\npolice,\npd,\n50,\nfive-o,\nhelp,\ndanger,\ndangerous,\nsource,\nignition,\ndetonate,\ndirty bomb,\ndrug,\nmedical,\nmedicine,\ndisrupt,\ncasualty,",

      "geo": "-73.424377,40.601441,-71.825867,41.237544",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151013180834_",

      "name": "SCH Campaign",

      "totalCount": 23300,

      "language": "",

      "curator": "mimran15",

      "collectionCreationDate": 1444738402000,

      "endDate": 1445920602000,

      "startDate": 1446274474000,

      "keywords": "Doha Health, Doha healthy, Doha vital, Doha Sidra, Doha HMC, Doha Al Ahli, Doha Al-Ahli, Doha hospital, Doha ambulance, Doha clinic, Doha food, Doha eat, Doha dinner, Doha lunch, Doha breakfast, Doha smoking, Doha shisha, Doha cigarette, Doha smoke, Doha obesity, Doha obese, Doha overweight, Doha fat, Doha sports, Doha gym, Doha fitness, Doha jogging, Doha running, Doha walking, Doha diabetes, Doha diabetic, Doha blood sugar, Doha insulin, Qatar Health, Qatar healthy, Qatar vital, Qatar Sidra, Qatar HMC, Qatar Al Ahli, Qatar Al-Ahli, Qatar hospital, Qatar ambulance, Qatar clinic, Qatar food, Qatar eat, Qatar dinner, Qatar lunch, Qatar breakfast, Qatar smoking, Qatar shisha, Qatar cigarette, Qatar smoke, Qatar obesity, Qatar obese, Qatar overweight, Qatar fat, Qatar sports, Qatar gym, Qatar fitness, Qatar jogging, Qatar running, Qatar walking, Qatar diabetes, Qatar diabetic, Qatar blood sugar, Qatar insulin, Wakra Health, Wakra healthy, Wakra vital, Wakra Sidra, Wakra HMC, Wakra Al Ahli, Wakra Al-Ahli, Wakra hospital, Wakra ambulance, Wakra clinic, Wakra food, Wakra eat, Wakra dinner, Wakra lunch, Wakra breakfast, Wakra smoking, Wakra shisha, Wakra cigarette, Wakra smoke, Wakra obesity, Wakra obese, Wakra overweight, Wakra fat, Wakra sports, Wakra gym, Wakra fitness, Wakra jogging, Wakra running, Wakra walking, Wakra diabetes, Wakra diabetic, Wakra blood sugar, Wakra insulin, SCHQatar, SahtakAwalan, Qatar_NSD, WCMCQ, aspirezone, QTRhealth, NamatQA, Aspetar, SEC_QATAR, SehaQatar, sidra, HMC_Qatar, QatarDiabetes, WISHQatar, Qatar_Biobank, Qatar_NSD, QRCS, QCHP_AD, AspireActive, UCQNursing, kullunaqatar, QatarHealthMag, ASH_Qatar, meddyco, AhliHospitalQr",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151015131835_testtsms",

      "name": "testtsms",

      "totalCount": 0,

      "language": null,

      "curator": "y09uc283",

      "collectionCreationDate": 1444884538000,

      "endDate": 1445090400000,

      "startDate": 1444916065000,

      "keywords": null,

      "geo": null,

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151017151641_european_refugee_crisis",

      "name": "European Refugee Crisis",

      "totalCount": 4245,

      "language": "en,nl,,nl",

      "curator": "HDataScienceLab",

      "collectionCreationDate": 1445077139000,

      "endDate": 1445252400000,

      "startDate": 1445077588000,

      "keywords": "refugee, europe, humanitarian aid, migrant, nederland, deutschland, ostereich",

      "geo": "-22.94,28.54,48.87,65.33",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151025044221_india2",

      "name": "india2",

      "totalCount": 1690,

      "language": "en",

      "curator": "teamdoitloud",

      "collectionCreationDate": 1445708580000,

      "endDate": 1446661331000,

      "startDate": 1446661402000,

      "keywords": "#india #modi",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151028104201_afghanistan",

      "name": "Afghanistan",

      "totalCount": 10,

      "language": "en",

      "curator": "ombaggio",

      "collectionCreationDate": 1446014650000,

      "endDate": 1446296401000,

      "startDate": 1446123144000,

      "keywords": "#eathquake",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151029162204_",

      "name": "Huracan Patricia",

      "totalCount": 20,

      "language": "en,es",

      "curator": "jorditost",

      "collectionCreationDate": 1446121814000,

      "endDate": 1446183865000,

      "startDate": 1446122783000,

      "keywords": "huracán patricia, huracán, patricia, mexico, flood, agua, lluvia, tormenta, herido, casa, calle, flood, comida, tifón, nuve, ciclón, tornado, viene, hurricane patricia, hurricane mexico",

      "geo": "-118.56,12.66,-85.28,33.85",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151030092757_terremoto_barcelona",

      "name": "Terremoto Barcelona",

      "totalCount": 5690,

      "language": "en,es,ca",

      "curator": "jorditost",

      "collectionCreationDate": 1446183865000,

      "endDate": 1446304693000,

      "startDate": 1446183928000,

      "keywords": "terremoto Barcelona, terratremol barcelona, earthquake barcelona, #terremotobarcelona, terratremol emporda, #terratremolemporda, #terremotoemporda, #terremotocataluña, #terremotocatalunya, terremoto cataluña, terremoto catalunya, terremoto emporda, terratremol emporda, temblor barcelona, terremoto cataluña, terremoto Cataluña, separatismo, indep*, #earthquakebarcelona, terremoto girona, terratremol girona, earthquake spain, earthquake catalonia, earthquake barcelona",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151031191537_komet_brandenburg",

      "name": "Komet Halloween",

      "totalCount": 715,

      "language": "de",

      "curator": "jorditost",

      "collectionCreationDate": 1446304686000,

      "endDate": 1446393601000,

      "startDate": 1446304713000,

      "keywords": "komet, #komet, komet berlin, comet berlin, komet dresden, komet erfurt, komet brandenburg",

      "geo": "2.8,44.8,18.8,56.8",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151101115304_",

      "name": "Cyclone Chapala",

      "totalCount": 48210,

      "language": "en",

      "curator": "maalhamadi123",

      "collectionCreationDate": 1446358600000,

      "endDate": 1446534002000,

      "startDate": 1446535372000,

      "keywords": "#Chapala, \n#Cyclone, \n#ChapalaCyclone, \n#yemen, \n#Oman, \n#ArabianSea, \n#Omancyclone,\nArabian Sea,",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151101121859_cyclone_chapala",

      "name": "Cyclone Chapala - Arabic",

      "totalCount": 0,

      "language": "ar",

      "curator": "maalhamadi123",

      "collectionCreationDate": 1446359353000,

      "endDate": null,

      "startDate": null,

      "keywords": "#سلطنةـعمان ,\n#شابالا ,\n#اليمن ,\nإعصار شابالا ,\n#اعصارـشابالا ,\n#شابالاـعمان ,\n#ظفار ,\n#متابعةـبحرـالعرب ,\nبحر العرب ,\n#حضرموت ,\n#اعصار ,\n#طقسـالعرب ,\n#شرورةـالآن ,\n#الخرير,\n#المكلا ,\n#شبوة ,\n#سقرطى ,\n#المهرة ,",

      "geo": "",

      "status": "TRASHED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151102105004_waterlek",

      "name": "Waterlek",

      "totalCount": 245,

      "language": "nl",

      "curator": "BWAntwerpen",

      "collectionCreationDate": 1446447098000,

      "endDate": 1446620404000,

      "startDate": 1446447121000,

      "keywords": "#waterlek, #rooseveltplaats, waterlek antwerpen, wateroverlast",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151105022136_india",

      "name": "Namo2",

      "totalCount": 0,

      "language": "en",

      "curator": "teamdoitloud",

      "collectionCreationDate": 1446661320000,

      "endDate": 1446661397000,

      "startDate": 1446661341000,

      "keywords": "#namo",

      "geo": "",

      "status": "STOPPED",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151105104604_dev_test",

      "name": "Dev Test",

      "totalCount": 2595,

      "language": "en",

      "curator": "y09uc283",

      "collectionCreationDate": 1446700592000,

      "endDate": 1446795159000,

      "startDate": 1446793998000,

      "keywords": "#winter, #india , #december, #snowfall",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151105122513_kushal_test",

      "name": "bihar_election",

      "totalCount": 0,

      "language": "en,de,it",

      "curator": "kushalkantgoyal",

      "collectionCreationDate": 1446706517000,

      "endDate": 1446706612000,

      "startDate": 1446707231000,

      "keywords": "#election, #polls, #biharPolls, bihar,ghghghg",

      "geo": "68.1,6.6,97.4,35.7",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   },

   {

      "code": "151106131539_fdfdf",

      "name": "fdfdf",

      "totalCount": 5,

      "language": "en",

      "curator": "sushant_dahiya",

      "collectionCreationDate": 1446795916000,

      "endDate": 1446795922000,

      "startDate": 1446795928000,

      "keywords": "dfdf",

      "geo": "",

      "status": "NOT_RUNNING",

      "labelCount": 0,

      "humanTaggedCount": null

   }

]

;
                     $scope.items = ['item1', 'item2', 'item3'];
/*
 * functions to create client side pagination
 */
    $scope.buildPager = function () {
      $scope.pagedItems = [];
      $scope.itemsPerPage = 4;
      $scope.currentPage = 1;
      $scope.figureOutItemsToDisplay();
    };

    $scope.figureOutItemsToDisplay = function () {
      $scope.filteredItems = $filter('filter')($scope.alphabet, {
        $: $scope.search
      });
      $scope.filterLength = $scope.filteredItems.length;
      var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
      var end = begin + $scope.itemsPerPage;
      $scope.pagedItems = $scope.filteredItems.slice(begin, end);
    };

    $scope.pageChanged = function () {
      $scope.figureOutItemsToDisplay();
    };

$scope.buildPager();

  $scope.animationsEnabled = true;

  $scope.open = function (letter) {

    var modalInstance =  $uibModal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'myModalContent.html',
      controller: 'ModalInstanceCtrl',
        windowClass: 'center-modal',
      size: 'lg',
      scope:$scope,
      resolve: {
        items: function () {
          return letter;
        }
      }
    });

    modalInstance.result.then(function (selectedItem) {
      $scope.selected = selectedItem;
    }, function () {
      $log.info('Modal dismissed at: ' + new Date());
    });
  };

  $scope.toggleAnimation = function () {
    $scope.animationsEnabled = !$scope.animationsEnabled;
  };


  
});
angular.module('DataApp').controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, items) {
console.log(items);
  $scope.user = items;
  $scope.selected = {
    item: $scope.alphabet[0]
  };

  $scope.ok = function () {
    $uibModalInstance.close($scope.selected.item);
  };

  $scope.cancel = function () {
    $uibModalInstance.dismiss('cancel');
  };
});


