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
- Bæta við möguleika til að eyða plöntu úr þínum plöntum
- Frekari vinna í Dialogum, t.d. óvirkja takka við ákveðnar aðstæður og laga útlit
- Bæta við fleiri möguleikum tengda notanda, t.d. að breyta lykilorði og eyða aðgangi
- Bæta við möguleika á að breyta þema forritsins
- Gefa betri viðbrögð við aðgerðum notanda, t.d. láta skilaboð birtast þegar notandi endurtekur lykilorð
  ekki rétt við nýskráningu, þegar hann reynir að breyta nafni plöntu í eitthvað sem ekki má eða
  þegar hann gerir eitthvað annað sem ekki er leyfilegt. Passa að hafa viðbrögð þegar ýtt er á dag á dagatali!
- Laga hvernig dagsetningar (LocalDate) eru birtar
- Setja einhverja reglu fyrir gild lykilorð
- Laga comparator til að geta raðað eftir íslenska stafrófinu
- Birta Dagatal þegar vökvunarsaga plöntu er skoðuð, í staðin fyrir listview
- Breyta hvernig upplýsingar eru lesnar úr skrá í upphafi. Núna er það úr .txt skrá, og ekki endilega
  skilvirkasta leiðin
- athuga hvort betra sé að hafa graphic á tökkum fyrir myndir eða bakgrunn. Held að background image sé betra, hægt að
  nota css
- taka kannski út myndirnar perlur.png og venus.png, ef perlur2.png og venus2.png eru samþykktar
- lesa inn fræðsluefnið, núna inniheldur Fraedsla.java strengi
- sýna að dagatal er óvirkt meira en þrjá mánuði fram í tímann. Setja kannski annan lit á dagana, og ljósari lit á letur
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
