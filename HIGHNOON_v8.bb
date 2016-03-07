Graphics 800,600,32,2 ;mode
SeedRnd MilliSecs()


Global bullet_y_temp, bullet_x_temp

AppTitle "COMMODORE 64 HIGHNOON, 1986 OCEAN (tm)"

Global pic = LoadImage("gfx\logo.png")
Global kauppala = LoadImage("gfx\city.png")

;While Not KeyHit(1)
;Cls
;DrawImage kauppala,0,0
;Text 0,10,"F1 HIGHNOON FULL SCREEN MODE"
;Text 0,40,"F2 WINDOWED SCREEN MODE"

;	If KeyHit(59) Then Graphics 800,600,32,1:Exit;moodi
;	If KeyHit(60) Then Graphics 800,600,32,2:Exit;moodi
	Graphics 800,600,32,2
	
;Flip
;Wend
SetBuffer BackBuffer()
Global leveli = 0

Global cave = LoadImage("gfx\cave.png")
Global basic = LoadImage("gfx\basic.png")
Global cover = LoadImage("gfx\Highnoon_cover.png")

Global wave = 1

Dim credit$(36);lopputekstin pituus
Global lopputekstit = MilliSecs()
Global font = LoadFont("font\C64_Pro_Mono-STYLE",24,True,False,False)
Global kuva = LoadImage("gfx\city.png")
Global buildings = LoadImage("gfx\buildings.png")
MaskImage buildings,0,0,0

Global ovi = LoadImage("gfx\ovi.png")
Global build1 = LoadImage("gfx\build1.png")
Global build2 = LoadImage("gfx\build2.png")
Global build3 = LoadImage("gfx\build3.png")
Global build4 = LoadImage("gfx\build4.png")

Global alku = LoadImage("gfx\Highnoon.png")
Global alku2 = LoadImage("gfx\High_noon2.png")
;Global musa = LoadSound("highnoon.mid")
Global kuva2 = LoadImage("gfx\sheriff.png")
Global sheriffi = LoadAnimImage("gfx\moves.png",55,55,0,19)
Global rosvokuva = LoadAnimImage("gfx\bandit.png",55,55,0,19)
MaskImage sheriffi,50,50,50
MaskImage rosvokuva,50,50,50
Global x = 155, y = 265, freimi = 6
Global luoti_x, luoti_y
Global liike_ajastin = MilliSecs()
Global lentaa = 0
Global freimi_suunta
Global bag = LoadImage("gfx\bag.png") 


Type ROSVO
	Field rosvo_x 
	Field rosvo_y
	Field rosvo_freimi
	Field rosvo_freimi_suunta = 0
	Field rosvo_liikkuu
	Field rosvon_kohde
	Field rosvo_ajastin
	Field rosvo_ajastin2
	Field rosvo_liike_ajastin
	Field rosvon_lentaa 
	Field rosvo_suunta
	Field rk
	Field ad
	Field moneybag = 0
	Field id
	Field rosvo_ampuu = 0
	Field rosvo_ampuu_ajastin
	Field ampu_x
	Field ampu_y
	Field ampu_suunta
	Field ampu_lentaa
End Type 


Global a,b,c,d, soun
Global soundit = 1
Global so$ = " ON"

Global varitunniste = 0
Global varitunniste_oikea = 0
Global varitunniste_vasen = 0

Global osui = 0
Global rosvoja_liikkeella = 0
Global rosvot_done = 0
Global rosvoja_max = 3						;ROSVOJA MAX RUUDUSSA
Global rosvoja_reservissa = 5				;rosvoja max
Global pointsit
Global bonus

Global saloonovet = LoadAnimImage("gfx\ovet.png",61,70,0,4)
Global ovetframe = 1
Global ovetajastin = MilliSecs()
Global ovet_alku_ajastin
Global ovet_heiluu = 0

Global wavefontti = LoadFont("font\C64_Pro-STYLE.ttf",32,1,0,0)
Global jailout
Global sheriffiin_osuu
Global nollaus_ajastin = MilliSecs()

	;oikaisu koodailuvaiheessa                   --------------------------->
	Goto oikotie


Cls
DrawImage basic,5,7
Flip
Delay 3500

Cls
SetFont font
Text 300,100,"LOADING HIGHNOON ...... "
DrawImage cover,250,120
Flip
Delay 5000

FlushKeys()



While (Not KeyHit(1)) ; p‰‰valikkosilmukka alkaa
	Cls

	cB = PlayMusic("music\theme.mp3")

	While (Not KeyHit(29))
		Cls
		DrawImage alku,0,0
		If KeyHit(1) Then End

		Flip
	Wend

	StopChannel cB
	FlushKeys()


	.oikotie ;                                <------------------------------

While Not KeyHit(28)
	aloitus()
	If jailout = 7 Then Exit
Wend
cB = PlayMusic("music\forsake.mp3")	:
	
	;luodaan rosvot (montako)
	For tempy = 1 To rosvoja_reservissa
		level.rosvo = New Rosvo
		
		level\rosvo_suunta = Rand(0,100)
		If (level\rosvo_suunta => 50) Then 
			level\rosvo_x = 850
		Else
			level\rosvo_x = -70
		EndIf
	
		level\rosvo_liikkuu = 1
		level\rosvo_ajastin = MilliSecs()
	
		level\rosvo_y = Rnd(330,450)
		level\rosvo_liike_ajastin = MilliSecs()
		level\rosvon_kohde = Rnd(0,10)  
		level\rk = 0 ;kohde
		level\ad = Rnd(1000,15000)
		level\id = tempy
	Next
		 



	;p‰‰silmukka - - - - - - - - - - - - - -  -  -  -   -   -   -    -      -          
	While (Not KeyHit(1))
		Cls
	

		If leveli = 0 Then 
			DrawImage kuva,0,0
		Else
			DrawImage cave,0,0
		EndIf

		aani()
		
		;ukkojen piirrot
		DrawImage saloonovet,290,243,ovetframe
		DrawImage sheriffi,x,y,freimi	
	
		;buildingit p‰‰lle
		If y < 265 Then DrawImage buildings,0,0 : DrawImage saloonovet,290,243,ovetframe	
	

		rosvojen_muuvit()	


		If ImagesCollide(sheriffi,x,y,freimi,saloonovet,290,243,ovetframe) And ovet_heiluu = 0 And y > 268 And y < 277Then 
			ovet_alku_ajastin = MilliSecs() 
			ovet_heiluu = 1
		EndIf
	
		
		If ovet_heiluu = 1 Then saluunan_ovet_heiluu()
		
			
		;luoti
		If lentaa = 1 Then DrawImage sheriffi, luoti_x, luoti_y, 16

		rosvo_liikkuu()
		seinien_tunnistus()
		If (freimi <> 17) And (freimi <> 18) Then sheriffin_liike()
		ammus_lentaa()

		Color 0,0,0
		Text 150,0,"jailout: "+jailout+"    wave: "+wave
		Text 0,500,rosvoja_liikkeella + " bandits out"
		SetFont font
		Text 115,5,pointsit + "                                                                           " + bonus

		If rosvot_done = rosvoja_reservissa And MilliSecs() > nollaus_ajastin+3000 Then nollaus() 
		
		If KeyHit(1) Then Exit
		
		Flip
	Wend
	;--- --- --- - - - - - - - - - - - - - -  -  -  -   -   -   -    -      -
	


	lopputekstit()
	StopChannel cB
Wend



;alkaa funktiot
Function rosvojen_muuvit()

		For level.rosvo = Each rosvo
			;Text level\rosvo_x, level\rosvo_y, level\id
			If (level\rosvo_liikkuu > 1) Then  
				DrawImage rosvokuva, level\rosvo_x, level\rosvo_y, level\rosvo_freimi
				If level\moneybag = 1 Then DrawImage bag,level\rosvo_x-5,level\rosvo_y+15 ;jos rosvo k‰vi pankissa, piirret‰‰n rahas‰kki
			EndIf
		
			;ammuksen liikutus ja piirto
			If level\rosvo_ampuu > 1 Then
			
				Select level\ampu_suunta
				Case 0
					level\ampu_x = level\ampu_x - 7
				Case 2
					level\ampu_x = level\ampu_x + 7					
				Case 5
					level\ampu_y = level\ampu_y - 6
				Case 6
					level\ampu_y = level\ampu_y + 6
				
				Case 8
					level\ampu_y = level\ampu_y - 5
					level\ampu_x = level\ampu_x + 7
				Case 10
						level\ampu_y = level\ampu_y - 5
					level\ampu_x = level\ampu_x - 7
				Case 12
					level\ampu_y = level\ampu_y + 5
					level\ampu_x = level\ampu_x - 7
				Case 14
					level\ampu_y = level\ampu_y + 5
					level\ampu_x = level\ampu_x + 7
				End Select
		
	
				DrawImage rosvokuva, level\ampu_x, level\ampu_y, 16
			EndIf
			
				;sheriffiin osuu
			If sheriffiin_osuu = 0 And ImagesCollide(sheriffi,x,y,freimi,rosvokuva,level\ampu_x,level\ampu_y,16) Then ;------------
			 	freimi = Rnd (17,18): sheriffiin_osuu = 1
				StopChannel cB
				If soundit = 1 Then cB = PlayMusic("music\death.mp3")
				ajastin = MilliSecs()
			EndIf
			
			If sheriffiin_osuu = 1 And MilliSecs() > ajastin + 2000 Then
				If KeyDown(28) Then 
					StopChannel cB
					sheriffiin_osuu = 0 : x = 400 : y = 500 : freimi = 7
					If soundit = 1 Then cB = PlayMusic("music\forsake.mp3")
				EndIf
			EndIf
		
			;saluunan ovet	
			If ImagesCollide(rosvokuva,level\rosvo_x, level\rosvo_y, level\rosvo_freimi, saloonovet,290,243,ovetframe) And ovet_heiluu = 0 And level\rosvo_y > 268 And rosvo_y < 277Then 
				ovet_alku_ajastin = MilliSecs() 
				ovet_heiluu = 1
			EndIf

;			;buildingit p‰‰lle
			If level\rosvo_y < 264 Then	 
					DrawImage saloonovet,290,243,ovetframe	

				If (level\rosvo_suunta > 50) Then ; rosvo tulee oikealta
					If level\rk > 400 Then
						DrawImage build3,0,0
					Else
						DrawImage build1,0,0
					EndIf

				Else ; rosvo tulee vasemmalta
					If level\rk > 400 Then 
						DrawImage build4,0,0
					Else
						DrawImage build2,0,0
					EndIf
				EndIf
			

				If y > 260 Then			
					DrawImage sheriffi,x,y,freimi
				EndIf
			EndIf
		Next
End Function



Function sheriffin_liike()

	;yl‰oikea
	If (KeyDown(200) And KeyDown(205)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 8) Then 
				freimi = 8
			Else 
				freimi = 9
			EndIf
			If (y > 255) And varitunniste = 1 Then y = y - 5
			If (x < 740) And varitunniste_oikea = 1 Then x = x + 5
		EndIf		
	EndIf
	;yl‰mummo
	If (KeyDown(200) And KeyDown(203)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 10) Then 
				freimi = 10
			Else 
				freimi = 11
			EndIf
			If (y > 255) And varitunniste = 1 Then y = y - 5
			If (x > 20) And varitunniste_vasen = 1 Then x = x - 5
		EndIf		
	EndIf
	;alaoikea
	If (KeyDown(208) And KeyDown(205)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 14) Then 
				freimi = 14
			Else 
				freimi = 15
			EndIf
			If (y < 550) And (varitunniste = 1 Or y > 260) Then y = y + 5
			If (x < 740) And varitunniste_oikea = 1 Then x = x + 5
		EndIf		
	EndIf
   ;alavasen
	If (KeyDown(208) And KeyDown(203)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 12) Then 
				freimi = 12
			Else 
				freimi = 13
			EndIf
			If (y < 550) And (varitunniste = 1 Or y > 260) Then y = y + 5
			If (x > 20) And varitunniste_vasen = 1 Then x = x - 5
		EndIf		
	EndIf

	;ammuskelu
	If (lentaa = 0) Then If (KeyDown(29)) Then
		lentaa = 1

		;luodin l‰htˆkohta
		Select freimi
		Case 0, 1
			luoti_x = x - 5
			luoti_y = y
		Case 2, 3
			luoti_x = x + 20
			luoti_y = y
		Case 4, 5
			luoti_x = x + 17
			luoti_y = y - 17
		Case 6, 7 
			luoti_x = x - 15 
			luoti_y = y 
		Case 8, 9
			luoti_x = x + 10 
			luoti_y = y - 10
		Case 10, 11
			luoti_x = x - 10 
			luoti_y = y - 10
		Case 12, 13
			luoti_x = x - 10
			luoti_y = y + 5
		Case 14, 15
			luoti_x = x + 10
			luoti_y = y + 10
		End Select
		
		;oikea 2,3      vasen 0,1      ylˆs 4,5      		alas 6,7
		;yl‰oikea 8,9	yl‰vasen 10,11	alaoikea 14,15		alavasen 12,13		
		freimi_suunta = freimi
	EndIf

;oikea
	If (KeyDown(205)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 2) Then 
				freimi = 2
			Else 
				freimi = 3
			EndIf
			If (x < 740) And varitunniste_oikea = 1 Then x = x + 5
		EndIf		
	EndIf
	;vasen
	If (KeyDown(203)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 0) Then 
				freimi = 0
			Else 
				freimi = 1
			EndIf
			If (x > 20) And varitunniste_vasen = 1 Then x = x - 5
		EndIf		
	EndIf
	;ylˆs
	If (KeyDown(200)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 5) Then 
				freimi = 5
			Else 
				freimi = 4
			EndIf
			If (y > 255) And varitunniste = 1 Then y = y - 5
		EndIf		
	EndIf
	;alas
	If (KeyDown(208)) Then
		If (MilliSecs() > liike_ajastin + 100) Then
			liike_ajastin = MilliSecs()
			If (freimi <> 6) Then 
				freimi = 6
			Else 
				freimi = 7
			EndIf
			If (y < 550) And (varitunniste = 1 Or y > 260) Then y = y + 5
		EndIf		
	EndIf

End Function


Function seinien_tunnistus()
	;seinien tunnistus
	varitunniste = 1
	varitunniste_oikea = 1:a = 1: b = 1
	varitunniste_vasen = 1:c = 1: d = 1
	GetColor(x+20,y+43)

	If y-10 < 265 Then
		If ColorRed() = 0 And ColorBlue() = 0 And ColorGreen() = 0 Then 
			varitunniste = 1
		Else
			varitunniste = 0
		EndIf

		If (y < 260) Then
			;oikealle
			GetColor(x+40,y+45)
	
			If ColorRed() = 10 And ColorBlue() = 10 And ColorGreen() = 10 Then 
				a = 0
			Else
				a = 1
			EndIf
			GetColor(x+43,y+45)
			
			If ColorRed() = 10 And ColorBlue() = 10 And ColorGreen() = 10 Then 
				b = 0
			Else
				b = 1
			EndIf
			
			If b = 0 Or a = 0 Then varitunniste_oikea = 0	


			;vasemmalle
			GetColor(x,y+45)
		
			If ColorRed() = 10 And ColorBlue() = 10 And ColorGreen() = 10 Then 
				c = 0
			Else
				c = 1
			EndIf
			GetColor(x-3,y+45)
			
			If ColorRed() = 10 And ColorBlue() = 10 And ColorGreen() = 10 Then 
				d = 0
			Else
				d = 1
			EndIf
	
			If c = 0 Or d = 0 Then varitunniste_vasen = 0	
	
		EndIf

	EndIf
End Function






Function ammus_lentaa()

	If (lentaa = 1) Then
		Select freimi_suunta
			Case 0
				luoti_x = luoti_x - 7
			Case 1
				luoti_x = luoti_x - 7
			Case 2
				luoti_x = luoti_x + 7
			Case 3
				luoti_x = luoti_x + 7
			Case 4
				luoti_y = luoti_y - 5
			Case 5
				luoti_y = luoti_y - 5
			Case 6
				luoti_y = luoti_y + 5
			Case 7
				luoti_y = luoti_y + 5
			Case 8
				luoti_y = luoti_y - 5
				luoti_x = luoti_x + 7
			Case 9
				luoti_y = luoti_y - 5
				luoti_x = luoti_x + 7
			Case 10
				luoti_y = luoti_y - 5
				luoti_x = luoti_x - 7
			Case 11
				luoti_y = luoti_y - 5
				luoti_x = luoti_x - 7
			Case 12
				luoti_y = luoti_y + 5
				luoti_x = luoti_x - 7
			Case 13
				luoti_y = luoti_y + 5
				luoti_x = luoti_x - 7
			Case 14
				luoti_y = luoti_y + 5
				luoti_x = luoti_x + 7
			Case 15
				luoti_y = luoti_y + 5
				luoti_x = luoti_x + 7
		End Select
	EndIf

	For level.rosvo = Each rosvo
		If level\rosvo_freimi < 16 Then
			If ImagesCollide(sheriffi,luoti_x,luoti_y,16,rosvokuva,level\rosvo_x,level\rosvo_y,level\rosvo_freimi) Then 
				lentaa = 0
				luoti_y = 0
				osui = 1
				level\rosvo_liikkuu = 19 ; dead man not walking
				rosvot_done = rosvot_done + 1
				nollaus_ajastin = MilliSecs()
				
				pointsit = pointsit  + 100
				If bonus < 750 Then bonus = bonus + 50
				
				If ((level\rosvo_freimi < 4) Or (level\rosvo_freimi = 6) Or (level\rosvo_freimi = 7) Or (level\rosvo_freimi >  11)) Then
					level\rosvo_freimi = 17
					level\rosvo_y = level\rosvo_y + 5
				Else	
					level\rosvo_freimi = 18
					level\rosvo_y = level\rosvo_y
				EndIf
					
				rosvoja_liikkeella = rosvoja_liikkeella - 1

			EndIf		
		EndIf
	Next
	
	If ((luoti_x < -20) Or (luoti_x > 800) Or (luoti_y < 250) Or (luoti_y > 600)) Then lentaa = 0 : luoti_y = 0
End Function




; rosvon liikkeet --------------------------------

Function rosvo_liikkuu()
;rosvo_liikkuu = 2
;rosvo_x = 450
;rosvo_y = 270

	;rosvo
For level.rosvo = Each rosvo 

	
	;If (MilliSecs() > level\rosvo_ajastin + 1000) Then level\rosvo_liikkuu = 1

	If (level\rosvo_liikkuu = 1) Then ;t‰ss‰ rosvo tehd‰‰n / regeneroituu, ellei niit‰ ole jo max-m‰‰r‰ ulkona
		If (MilliSecs() > level\rosvo_ajastin + level\ad) Then 
			If (rosvoja_liikkeella < rosvoja_max) Then 
				level\rosvo_liikkuu = 10 : rosvoja_liikkeella = rosvoja_liikkeella + 1
				level\rosvo_ajastin2 = MilliSecs()
			EndIf
		EndIf
	EndIf
	
	
	If (level\rosvo_liikkuu = 10) Then ; regeneroidaan viiveell‰
		If (MilliSecs() > level\rosvo_ajastin2 + 3000) Then level\rosvo_liikkuu = 2
		level\rosvo_freimi_suunta = 0
		level\rosvo_y = Rnd(330,450) ;regeneroidessa t‰ytyy antaa y-arvo uudestaan
	EndIf
	

If (MilliSecs() > level\rosvo_liike_ajastin + 100 And level\rosvo_liikkuu < 19) Then
		level\rosvo_liike_ajastin = MilliSecs()
		
	;rosvojen ammuskelu, osio 1
	If level\rosvo_liikkuu = 2 Or level\rosvo_liikkuu = 5 Then 
		If level\rosvo_ampuu = 0 Then level\rosvo_ampuu_ajastin = (MilliSecs() + Rnd(1000,6000)) : level\rosvo_ampuu = 1
	EndIf
		


	If level\rosvo_ampuu <> 2 Then 
		Select level\rosvo_liikkuu
	Case 2 ; rosvo l‰htee liikkeelle
			
			If (level\rosvo_suunta > 50) Then	 ; rosvo tulee oikealta
			
				If (level\rosvon_kohde < 6) Then 
					level\rk = 302 ; pankki vai saluuna
				Else
					level\rk = 505
				EndIf

				If (level\rosvo_y > 263)Then 
					If level\rosvo_x < level\rk+120 Then level\rosvo_y = level\rosvo_y - 3
				Else
					level\rosvo_liikkuu = 3
					;rosvo on mennyt sis‰lle
				EndIf

				
				If level\rosvo_x => level\rk Then 
					level\rosvo_x = level\rosvo_x - 5
					level\rosvo_freimi_suunta = 0
				Else
					level\rosvo_freimi_suunta = 4
				EndIf

	
			Else 			;rosvo tulee vasemmalta
				If (level\rosvon_kohde < 6) Then 
					level\rk = 295
				Else
					level\rk = 496
				EndIf
				
				If (level\rosvo_y > 263)Then 
					If level\rosvo_x > level\rk-120 Then level\rosvo_y = level\rosvo_y - 3
				Else
					level\rosvo_liikkuu = 3
					;rosvo on mennyt sis‰lle
				EndIf

				If level\rosvo_x =< level\rk Then
					level\rosvo_freimi_suunta = 2 
					level\rosvo_x = level\rosvo_x + 5
				Else
					level\rosvo_freimi_suunta = 4
				EndIf
				
			EndIf
	 Case 3; pankkirosvo on mennyt juuri pankin tai saluunan ovesta, mit‰ sis‰ll‰ tapahtuu?
		If (level\rosvo_suunta > 50) Then ; rosvo tulee oikealta
				If (level\rosvo_x > level\rk-80)Then 
					level\rosvo_x = level\rosvo_x - 3
					level\rosvo_freimi_suunta = 0
				Else
					level\rosvo_liikkuu = 4
					;If level\rk > 400 Then 
					level\moneybag = 1 ;pankkirosvolle s‰kki k‰teen
				EndIf
		Else ; rosvo tulee vasemmalta
				If (level\rosvo_x < level\rk+80)Then 
					level\rosvo_x = level\rosvo_x + 3
					level\rosvo_freimi_suunta = 2		
				Else
					level\rosvo_liikkuu = 4
					;If level\rk > 400 Then 
					level\moneybag = 1 ;pankkirosvolle s‰kki k‰teen
				EndIf
		EndIf  	
	Case 4	;k‰‰ntyy sis‰lt‰ takaisinp‰in
		If (level\rosvo_suunta > 50) Then ; rosvo tulee oikealta
				If (level\rosvo_x < level\rk) Then 
					level\rosvo_x = level\rosvo_x + 3
					level\rosvo_freimi_suunta = 2
				Else
					level\rosvo_liikkuu = 5
				EndIf
				
		Else ; rosvo tulee vasemmalta
				If (level\rosvo_x => level\rk)Then 
					level\rosvo_x = level\rosvo_x - 3
					level\rosvo_freimi_suunta = 0
				Else
					level\rosvo_liikkuu = 5
				EndIf
		EndIf
	Case 5 ;Bandit tulee ulos ja l‰htee livohkaan
		If (level\rosvo_suunta > 50) Then ; rosvo tulee oikealta
				If (level\rosvo_x < 830) Then 
					If (level\rosvo_y > 290) Then 
						level\rosvo_x = level\rosvo_x + 3
						level\rosvo_freimi_suunta = 2
					Else
						level\rosvo_freimi_suunta = 6
					EndIf
					level\rosvo_y = level\rosvo_y + 3				
				Else
					level\rosvo_liikkuu = 18 ; rosvo p‰‰si livohkaan
					rosvoja_liikkeella = rosvoja_liikkeella - 1
					level\moneybag = 0 ; poistetaan s‰kki k‰dest‰
					level\rosvo_y = Rnd(330,450)
				EndIf
				
		Else ; rosvo tulee vasemmalta
				If (level\rosvo_x => -40)Then 
					If (level\rosvo_y > 290) Then 
						level\rosvo_x = level\rosvo_x - 3
						level\rosvo_freimi_suunta = 0
					Else
						level\rosvo_freimi_suunta = 6
					EndIf
					level\rosvo_y = level\rosvo_y + 3
				Else
					level\rosvo_liikkuu = 18 ; rosvo p‰‰si livohkaan
					rosvoja_liikkeella = rosvoja_liikkeella - 1
					level\moneybag = 0 ;poistetaan s‰kki k‰sist‰
					level\rosvo_y = Rnd(330,450)
				EndIf
		EndIf
	End Select
	EndIf
	
	
	
	;rosvojen ammuskelu, osio 2
	If (level\rosvo_y > 270 And level\rosvo_ampuu = 1 And MilliSecs() > level\rosvo_ampuu_ajastin + 50) Then

		;mihin suuntaan rosvo k‰‰ntyy ampuessaan

		If x < level\rosvo_x-20 Then 					
			If y < level\rosvo_y-20 Then 
				level\rosvo_freimi_suunta = 10
			Else If y > level\rosvo_y+20 Then
				level\rosvo_freimi_suunta = 12
			Else
				level\rosvo_freimi_suunta = 0
			EndIf						
		Else If x > level\rosvo_x+20
			If y < level\rosvo_y-20 Then 
				level\rosvo_freimi_suunta = 8
			Else If y > level\rosvo_y+20 Then
				level\rosvo_freimi_suunta = 14
			Else
				level\rosvo_freimi_suunta = 2		
			EndIf
		Else
			If y < level\rosvo_y Then
				level\rosvo_freimi_suunta = 5
			Else
				level\rosvo_freimi_suunta = 6
			EndIf 
		EndIf


		level\rosvo_ampuu = 2

		level\ampu_x = level\rosvo_x
		level\ampu_y = level\rosvo_y
		level\ampu_lentaa = 1
		
	EndIf

	;lenn‰tt‰‰ luodit
		If level\rosvo_ampuu > 1 Then
				;ammus_lentaa2()
			If level\ampu_lentaa = 1 Then 
				level\ampu_lentaa = 2
				level\ampu_suunta = level\rosvo_freimi_suunta
			EndIf
		
			If MilliSecs() > level\rosvo_ampuu_ajastin + 1200 Then level\rosvo_ampuu = 3	
		EndIf
			

	
	If (level\rosvo_ampuu > 1) Then 
		If ((level\ampu_x < 0) Or (level\ampu_x > 800) Or (level\ampu_y < 250) Or (level\ampu_y > 580)) Then level\rosvo_ampuu = 0 : level\ampu_lentaa = 0 : level\ampu_y = 260 : level\ampu_x = -30
	EndIf
	
	;nollataan ampumisen uusinta
	If MilliSecs() > level\rosvo_ampuu_ajastin + 6000 Then level\rosvo_ampuu = 0 : level\ampu_lentaa = 0
	
	
	
	;rosvo makaa maassa, noudetaan?
	If (level\rosvo_liikkuu > 17) Then
		
	Else ;rosvo ei makaa maassa	
		;rosvojen liike
		If (level\rosvo_ampuu <> 2) Then
			If (level\rosvo_freimi <> level\rosvo_freimi_suunta) Then
				level\rosvo_freimi = level\rosvo_freimi_suunta
			Else
				level\rosvo_freimi = level\rosvo_freimi_suunta+1
			EndIf
			
		Else
			level\rosvo_freimi = level\rosvo_freimi_suunta
		EndIf
	
	EndIf
	
	EndIf
		


Next 

End Function


Function lopputekstit()
		Local final = LoadImage("gfx\Highnoon_title.png")

		Local creditlen = 36
		Local y = 600
		Color 255,255,255
		;               01234567890123456789012345678901
		credit$(0)   =  "                              "
		credit$(1)   =  "                              "
		credit$(2)   =  "       H I G H N O O N        "
		credit$(3)   =  "                              "
		credit$(4)   =  "    faithful to original      "
		credit$(5)   =  "      made by Ocean (tm)      "
		credit$(6)   =  "                              "
		credit$(7)   =  "          -----------         "
		credit$(8)   =  "         LONELY RIDER         "
		credit$(9)   =  "           presents           "
		credit$(10)  =  "           		           "
		credit$(11)  =  "            GRAPHICS          "
		credit$(12)  =  "         Jarno Puikkonen      "
		credit$(13)  =  "         Vesa Vertainen       "
		credit$(14)  =  "                              "
		credit$(15)  =  "          MUSIC AS.MP3        "
		credit$(16)  =  "         Jarno Puikkonen      "
		credit$(17)  =  "         originally.sid       "
		credit$(18)  =  "           David Dunn         "
		credit$(19)  =  "            ------            "
		credit$(20)  =  "     						   "
		credit$(21)  =  "                              "
		credit$(22)  =  "          1986 / 2015         "
		credit$(23)  =  "        ---------------       "
		credit$(24)  =  "							   "
		credit$(25)  =  "                              "
		credit$(26)  =  "______________________________"
		credit$(27)  =  "   Thanks to Ocean Software   "
		credit$(28)  =  "      for original game       "
		credit$(29)  =  "                              "
		credit$(30)  =  "    We hope that our game     "
		credit$(31)  =  "     doesn¥t violate any      "
		credit$(32)  =  "        coding rights.        "
		credit$(33)  =  "                              "
		credit$(34)  =  "                              "
		credit$(35)  =  "                              "
		
		Color 0,0,255
;		loparit = LoadFont("last Ninja", 20, False, False, False)
; 		SetFont loparit

		Local lisays = 0, alku = 0, creditlen2 = 2, more = 0, more2 = 0, vah = 0, timera = MilliSecs()
		i = 0
		FlushKeys()
		While ((alku <> creditlen) And (Not KeyHit(1)))
		
			If (MilliSecs() > timera + 10) Then
			Cls	
					DrawImage final,0,0
					While ((i<creditlen2) And (Not KeyHit(1)))
										
					Text 400,y+lisays,credit$(i),True		;! tekstin keskitys !
					lisays = lisays + 30
					i = i + 1
				Wend
		
				If ((y+15) < -10) Then vah = 1;aloitetaan yl‰osasta v‰hent‰m‰‰n tekstej‰
				y = y - 1 ;siirret‰‰n teksti‰ ylˆs
				lisays = 0
				more = more + 1
				more2 = more2 + 1
				
				If (more2 => 30) Then
					If (vah = 1) Then 
						alku = alku + 1
						y = y + 30
						more2 = 0
					EndIf
				EndIf
				If (more = 15) Then
					creditlen2 = creditlen2 + 1
					If (creditlen2>36) Then creditlen2 = 36
					more = 0
				EndIf
				i = alku
				timera = MilliSecs()
				Flip
			EndIf	
		Wend

Delay 100
timera = MilliSecs()
i = 0
alku = 0
While (i > -1 And (Not KeyHit(1)))
Cls
	If (MilliSecs() > (timera + 100)) Then
		If (alku = 0) Then 
			i = i + 1
		Else
			i = i - 1
		EndIf

		If (i = 3) Then 
			i = 2
			Delay 200
			alku = 1
		EndIf
	
		timera = MilliSecs()
	EndIf
Flip
Wend

End Function

Function aani()
	If KeyDown(50) And soun = 0 Then
		soun = 1
		If (soundit = 1) Then
			soundit = 0
			ChannelVolume cB,.0
			so$ = "OFF"
		Else
			soundit = 1
			ChannelVolume cB,.9
			so$ = " ON"
		EndIf 
	EndIf
	If Not KeyDown(50) Then soun = 0
	
	Color 0,0,0
	Text 0,510,"MUSIC: "+so$
	
;	If (Not ChannelPlaying (cB)) And (sheriffiin_osuu = 0) And soundit = 1 Then cB = PlayMusic("music\forsake.mp3")
End Function


; Last score.dat       highscore.dat
;------------------------------------
; text 1st Wave

; text 2nd Wave

; text 3rd Wave

; text 4th Wave

; text 5th Wave

;While (Not KeyHit(57))
;Cls
;Flip
;Wend

;Wend


Function aloitus()
	Cls
		If leveli = 0 Then 
			DrawImage kuva,0,0
			DrawImage saloonovet,290,243,1
		Else
			DrawImage cave,0,0
		EndIf
		
		
		Select jailout
		Case 0
			If (MilliSecs() > liike_ajastin + 1000) Then jailout = 1 : liike_ajastin = MilliSecs()
		Case 1
			DrawImage ovi,0,0
			If (MilliSecs() > liike_ajastin + 1000) Then jailout = 2 : liike_ajastin = MilliSecs()
		Case 2
			If (MilliSecs() > liike_ajastin + 1000) Then jailout = 3 : liike_ajastin = MilliSecs()
		Case 3
			If (MilliSecs() > liike_ajastin + 130) Then
				If (freimi = 7) Then 
					freimi = 6
				Else 
					freimi = 7
				EndIf
				y = y + 5
				
				If y > 280 Then jailout = 4
				liike_ajastin = MilliSecs()
			EndIf			
		Case 4
			If (MilliSecs() > liike_ajastin + 130) Then
				If (freimi = 14) Then 
					freimi = 15
				Else 
					freimi = 14
				EndIf
				y = y + 5
				x = x + 5
				
				If y > 450 Then jailout = 5
				liike_ajastin = MilliSecs()
			EndIf
			
		Case 5
			If (MilliSecs() > liike_ajastin + 130) Then
				If (freimi = 2) Then 
					freimi = 3
				Else 
					freimi = 2
				EndIf

				x = x + 5
				
				If x > 390 Then jailout = 6
				liike_ajastin = MilliSecs()
			EndIf	
			SetFont wavefontti : Color 0,0,0
			
			Select wave
			Case 1	
				Text 350,515,"1ST WAVE"
			Case 2
				Text 350,515,"2ND WAVE"
			Case 3
				Text 350,515,"3RD WAVE"
			Case 4
				Text 350,515,"4TH WAVE"
			End Select
		Case 6
			freimi = 4
			If (MilliSecs() > liike_ajastin + 400) Then jailout = 7 : liike_ajastin = MilliSecs
			SetFont wavefontti : Color 0,0,0
			Select wave
			Case 1	
				Text 350,515,"1ST WAVE"
			Case 2
				Text 350,515,"2ND WAVE"
			Case 3
				Text 350,515,"3RD WAVE"
			Case 4
				Text 350,515,"4TH WAVE"
			End Select
			
		End Select
		
		If jailout > 0 Then DrawImage sheriffi, x, y, freimi		
	Flip
End Function


Function saluunan_ovet_heiluu()
	If MilliSecs() > ovetajastin + 100 Then
		ovetframe = ovetframe + 1
		If ovetframe = 4 Then ovetframe = 0
		ovetajastin = MilliSecs()
		
		If (MilliSecs() > ovet_alku_ajastin + 2000) Then ovet_heiluu = 0 : ovetframe = 1
	EndIf
End Function


Function nollaus()

	For level.rosvo = Each rosvo
	;rosvon nollaus
			level\rosvo_ajastin = MilliSecs()
			level\ad = Rnd(2000,4000)
			
			level\rosvo_suunta = Rand(0,100)
			If (level\rosvo_suunta => 50) Then 
				level\rosvo_x = 850
			Else
				level\rosvo_x = -70
			EndIf
	
			level\moneybag = 0

			level\rosvo_ajastin = MilliSecs()
			level\rosvo_ajastin2 = MilliSecs()

			level\rosvo_liikkuu = 1

			level\rosvo_y = Rnd(330,450)
			level\rosvo_liike_ajastin = MilliSecs()
			level\rosvon_kohde = Rnd(0,10)
			level\ad = Rnd(1000,15000)

			level\rosvo_freimi = 0
			level\rosvo_freimi_suunta = 0
	Next
	
	liike_ajastin = MilliSecs()
	rosvot_done = 0
	rosvoja_liikkeella = 0
	nollaus_ajastin = MilliSecs()
	jailout = 0
	x = 155
	y = 265
	freimi = 6
	wave = wave + 1
	
	While Not KeyHit(28)
		aloitus()
		If jailout = 7 Then Exit
	Wend
	If Not ChannelPlaying(cB) Then cb = PlayMusic("music\forsake.mp3")
End Function