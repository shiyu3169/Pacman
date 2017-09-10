/*(a)*/
wife(queen_Elizabeth_II, philip).
son(charles, queen_Elizabeth_II).

son(charles, philip).
wife(diana, charles).
wife(camilla, charles).

daughter(anne, queen_Elizabeth_II).
daughter(anne, philip).
wife(anne, captain_Mark_Phillips).
wife(anne, timothy_Laurence).

son(andrew, queen_Elizabeth_II).
son(andrew, philip).
wife(sarah, andrew).

son(edward, queen_Elizabeth_II).
son(edward, philip).
wife(sophie, edward).

son(william, diana).
son(william, charles).
wife(kate, william).

son(harry, diana).
son(harry, charles).

son(peter_Phillips, captain_Mark_Phillips).
son(peter_Phillips, anne).
wife(autumn_Phillips, peter_Phillips).

daughter(zara_Phillips, captain_Mark_Phillips).
daughter(zara_Phillips, anne).
wife(zara_Phillips, mike_Tindall).

daughter(beatrice, andrew).
daughter(beatrice, sarah).

daughter(eugenie, andrew).
daughter(eugenie, sarah).

daughter(louise, edward).
daughter(louise, sophie).

son(james, edward).
son(james, sophie).

son(george, william).
son(george, kate).

daughter(savannah, autumn).
daughter(savannah, peter_Phillips).

daughter(isla, autumn).
daughter(isla, peter_Phillips).

son(mia_Grace, zara_Phillips).
son(mia_Grace, mike_Tinkdall).

/*(b)*/
husband(A, B) :- wife(B, A).

spouse(A, B) :-
    wife(A,B);
    wife(B, A).

child(A, B) :-
    daughter(A, B);
    son(A, B).

parent(A, B) :-
    daughter(B, A);
    son(B, A).

grandChild(A,C) :-
    (daughter(A, B); son(A, B)),
    (daughter(B, C); son(B, C)).

greatGrandParent(A, D) :-
    (daughter(B, A); son(B, A)),
    (daughter(C, B); son(C, B)),
    (daughter(D, C); son(D, C)).

greatGrandChild(A, D) :-
    (daughter(C, D); son(C, D)),
    (daughter(B, C); son(B, C)),
    (daughter(A, B); son(A, B)).

brother(A, B) :-
    son(A, C), son(B, C).

sister(A, B) :-
    daughter(A, C),
    daughter(B, C).

aunt(A, B) :-
    (daughter(A, D), (daughter(C, D); son(C, D)), (daughter(B, C); son(B , C)));
    (son(E, F), wife(A, E), (son(C, F); daughter(C, F)), (daughter(B, C); son(B , C))).

uncle(A, B) :-
    (son(A, D), (daughter(C, D); son(C, D)), (daughter(B, C); son(B , C)));
    (daughter(E, F), wife(E, A), (son(C, F); daughter(C, F)), (daughter(B, C); son(B , C))).


brotherInLaw(A, B) :-
    (wife(B, C), son(A, D), son(C, D));
    (wife(C, B), son(A, D), daughter(C, D)).

sisterInLaw(A, B) :-
    (wife(B, C), daughter(A, D), son(C, D));
    (wife(C, B), daughter(A, D), daughter(C, D)).

/*(c)*/
/*
 I) Who is Sarah’s husband?
 ?-husband(Who, sarah).
 Who = andrew.
 
 II)Who are Elizabeth’s great grandchildren?
 ?-greatGrandChild(Who, queen_Elizabeth_II).
 Who = mia_Grace;
 Who = savannah;
 Who = isla;
 Who = george;
 
 III) Who are Zara’s grandparents?
 ?-grandChild(zara_Phillips, Who).
 Who = queen_Elizabeth_II;
 Who = philip;
 
 IV) Who are Diana’s brothers and sisters-in-law?
 ?- brother(Bro, diana); sisterInLaw(SisterInLaw, diana).
 SisterInLaw = anne ;

 V) Who are Beatrice’s uncles?
 ?- uncle(Who, beatrice).

 VI)Who are Charles's nieces?
 ?- uncle(charles, Who),brother(charles,_),daughter(Who,_).
Who = zara_Phillips ;
Who = beatrice ;
Who = eugenie ;
Who = louise ;
  */
