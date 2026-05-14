megold(P) :-
    P = bajnoksag(
          b(dombos,_,_,_),
          b(gombos,_,_,_),
          b(kabos,_,_,_),
          b(lombos,_,_,_),
          b(zabos,_,_,_)
          ),
    
	bajnok(B1, P), vezeteknev(B1, gombos), helyezes(B1, GombosHelyezes), 
    bajnok(B2, P), keresztnev(B2, cecilia), helyezes(B2, CeciliaHelyezes),
    bajnok(B3, P), klub(B3, sztarklub), helyezes(B3, SztarklubHelyezes),
    bajnok(B4, P), vezeteknev(B4, zabos), helyezes(B4, ZabosHelyezes),
    bajnok(B5, P), keresztnev(B5, emilia), helyezes(B5, EmiliaHelyezes),
    bajnok(B6, P), vezeteknev(B6, dombos), helyezes(B6, DombosHelyezes),
    bajnok(B7, P), klub(B7, anyokak), helyezes(B7, AnyokakHelyezes),
  	bajnok(B9, P), vezeteknev(B9, lombos), helyezes(B9, LombosHelyezes),
    bajnok(B10, P), keresztnev(B10, amalia), helyezes(B10, AmaliaHelyezes),
    bajnok(B11, P), klub(B11, oregnenek), helyezes(B11, OregnenekHelyezes),
    
    % Rozália nem a Nagyi-Klub tagja
	bajnok(B8, P), keresztnev(B8, rozalia), nemnagyiklub(B8),
    
	% Kabos keresztneve nem Cecília és nem Rozália
	bajnok(B20, P), vezeteknev(B20, kabos), nemcecilianemrozalia(B20),
    
	% Lombos keresztneve nem Emília
	nememilia(B9),
    
	% Gombos nem az Öreg nénék képviseletében játszott
    nemoregnenek(B1),
    
	% Emília nem a Stár-Klub tagja
    nemsztarklub(B5),

    bajnok(B12, P), keresztnev(B12, otilia),
    bajnok(B13, P), klub(B13, mamikak),
    bajnok(B14, P), klub(B14, nagyiklub),
    
    
    bajnok(B15, P), helyezes(B15, 1),
    bajnok(B16, P), helyezes(B16, 2),
    bajnok(B17, P), helyezes(B17, 3),
    bajnok(B18, P), helyezes(B18, 4),
    bajnok(B19, P), helyezes(B19, 5),
    
    % Gombos jobb helyezést ért el, mint Cecília és mint a Sztár-Klub versenyzője, de rosszabat mint Zabos
    GombosHelyezes < CeciliaHelyezes,
    GombosHelyezes < SztarklubHelyezes, 
    GombosHelyezes > ZabosHelyezes,
    
    % Az Anyókák képviselője közvetlenül Emília előtt végzett, aki közvetlenül Dombos előtt végzett a bajnokságon
    AnyokakHelyezes =:= EmiliaHelyezes-1, 
    EmiliaHelyezes =:= DombosHelyezes-1,
    
    % Lombos valahol Amália előtt, aki valahol az Öreg Nénék versenyzője előtt végzett
    LombosHelyezes < AmaliaHelyezes, 
    AmaliaHelyezes < OregnenekHelyezes.



nememilia(X) :- keresztnev(X,amalia).
nememilia(X) :- keresztnev(X,cecilia).
nememilia(X) :- keresztnev(X,otilia).
nememilia(X) :- keresztnev(X,rozalia).

nemsztarklub(X) :- klub(X,anyokak).
nemsztarklub(X) :- klub(X,mamikak).
nemsztarklub(X) :- klub(X,nagyiklub).
nemsztarklub(X) :- klub(X,oregnenek).

nemoregnenek(X) :- klub(X,anyokak).
nemoregnenek(X) :- klub(X,mamikak).
nemoregnenek(X) :- klub(X,nagyiklub).
nemoregnenek(X) :- klub(X,sztarklub).

nemcecilianemrozalia(X) :- keresztnev(X,amalia).
nemcecilianemrozalia(X) :- keresztnev(X,emilia).
nemcecilianemrozalia(X) :- keresztnev(X,otilia).

nemnagyiklub(X) :- klub(X,anyokak).
nemnagyiklub(X) :- klub(X,mamikak).
nemnagyiklub(X) :- klub(X,oregnenek).
nemnagyiklub(X) :- klub(X,sztarklub).

bajnok(X, bajnoksag(X,_,_,_,_)).
bajnok(X, bajnoksag(_,X,_,_,_)).
bajnok(X, bajnoksag(_,_,X,_,_)).
bajnok(X, bajnoksag(_,_,_,X,_)).
bajnok(X, bajnoksag(_,_,_,_,X)).

vezeteknev(b(X,_,_,_), X).
keresztnev(b(_,X,_,_), X).
klub(b(_,_,X,_), X).
helyezes(b(_,_,_,X), X).