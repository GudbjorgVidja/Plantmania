# Plantmania

Stórt verkefni í viðmótsforritun vorið 2023. Þetta er plöntuforrit sem hjálpar notendum að sjá um plönturnar sínar.
Við misreiknuðum stærðina á forritinu rosalega, og náum því ekki að bæta öllu við sem við vildum. Forritið
keyrir og er nauðsynleg grunnvirkni til staðar, en það er ýmislegt sem við viljum bæta við, breyta og bæta.
Við ætlum að halda áfram með þetta eftir skil, og klára það á eigin forsendum.

Taka skal fram að ekki allar upplýsingarnar eru réttar.

### Viðbætur sem eiga eftir að koma:

- Bæta við fleiri plöntum
- Flipa með smá leiðbeiningum bætt við, til að útskýra t.d. hvað tákn og tölur á dagatali þýða
    - hafa það kannski undir notandi menuButton
- Bæta við möguleika til að eyða plöntu úr þínum plöntum. Ath að ef það á að gera það úr plöntuglugganum
  þarf hann að taka inn ObservableList af plöntum notanda
- Bæta við fleiri möguleikum tengda notanda, t.d. að breyta lykilorði og eyða aðgangi
- Bæta við möguleika á að breyta þema forritsins
- Gefa betri viðbrögð við aðgerðum notanda, t.d. láta skilaboð birtast þegar notandi endurtekur lykilorð
  ekki rétt við nýskráningu, þegar hann reynir að breyta nafni plöntu í eitthvað sem ekki má eða
  þegar hann gerir eitthvað annað sem ekki er leyfilegt. Passa að hafa viðbrögð þegar ýtt er á dag á dagatali!
- Setja einhverja reglu fyrir gild lykilorð
- Laga comparator til að geta raðað eftir íslenska stafrófinu
- Birta Dagatal þegar vökvunarsaga plöntu er skoðuð, í staðin fyrir listview
- Breyta hvernig upplýsingar eru lesnar úr skrá í upphafi. Núna er það úr .txt skrá, og ekki endilega
  skilvirkasta leiðin
- athuga hvort betra sé að hafa graphic á tökkum fyrir myndir eða bakgrunn. Held að background image sé betra, hægt að
  nota css
- passa að nafn plöntu geti ekki verið lengra en sést á spjaldi
    - lengd nickname má ekki vera lengri en eitthvað ákveðið
- setja max lengd á notandanafn
- breyta nafni á css, og laga uppsetningu
- laga fræðsluefni ef færi gefst
- setja stylesheet á dialoga
    - nýskráning dialog
    - í plöntuglugga
        - breyta nótum
        - breyting á nafni
        - vökvunarsaga
        - breyta tíma á milli vökvanna
    - smellt á dag í dqagatali
- í plöntuglugga er ekki augljóst að hægt sé að ýta á til að breyta nafni
- setja dialog til að bæta við vökvun í plöntuglugga, annars ekki augljóst að það sé búið að gerast þegar þú klárar
- skoða með að setja tooltip á takka á MinPlantaSpjald þegar þeir eru óvirkir. Virkar ekki venjulega ef takkarnir eru
  óvirkir, reyna kannski að setja á annan hlut sem inniheldur takkana eða eitthvað
- Í fræðslunni, lesa inn heila runu, og splitta á * eftir hvaða titledPane það er fyrir. Splitta svo á + til að pikka út
  titla og gera þá sæta
- Finna hvaða styleclass eru á dagatal
- Mögulegarf breytingar á dagatal og dag:
    - setja lítið hak fyrir framan loknar vökvanir
    - setja annað útlit á þann part dagatalsins sem ekki er notaður (þ.e. of langt fram í tímann),
      Setja kannski annan lit á dagana, og ljósari lit á letur
    - stækka letur á ártali og mánuð
    - stafla labelum fyrir loknar og óloknar vökvanir
    - gera styleclass sem er settur á daginn í dag
    - styleclass á valinn dag
- Binda stöðu dialoga miðað við aðalsenuna? Ef þú færir gluggann á annan skjá þá koma dialogar samt á þeim fyrsta.
- breyta leturgerð
- samhæfa leturstærðir, ekki hafa of margar mismunandi sýnilegar í einu
- hvað gerist ef tvær mismunadi keyrlsur eru í gangi á sama tíma fyrir sama aðgang?
- endurnefna plontuyfirlit sem yfirlit, því það getur innihaldið planta eða minPlanta

### Plöntur sem ætti að bæta við:

- gúmmítré
- rósir
- einhverjar mat-/kryddjurtir
- orkidea
- pothos
- þykkblöðungar

### Viðbætur sem hafa verið gerðar

- Ef síur eru stilltar þ.a. engir möguleikar eru sýnilegir í yfirliti, setja þá texta og kannski mynd sem bendir á það
- Breytingar vistast þegar forriti er lokað, ekki bara við útskráningu
- Þegar plöntu er bætt við mínar plöntur, ekki hafa dialog heldur kannski popup neðst sem fer eftir smá tíma
- lesa inn fræðsluefnið, núna inniheldur Fraedsla.java strengi
- taka kannski út myndirnar perlur.png og venus.png, ef perlur2.png og venus2.png eru samþykktar
- Laga hvernig dagsetningar (LocalDate) eru birtar
- Frekari vinna í Dialogum, t.d. óvirkja takka við ákveðnar aðstæður
- niðurtalning vökvana núllstillist við útskráningu!!!
- Endurnefna latnesktNafn sem fraediheiti
- Endurnefna almenntNafn sem almenntHeiti
-
