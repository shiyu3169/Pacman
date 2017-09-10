/*(a)*/
motorway(A, C):-
    m(A, C);
    m(C, A);
    (m(A, B), motorway(B, C));
    (m(C, B), motorway(B, A)).

    

footpath(A, C):-
    f(A, C);
    f(C, A);
    (f(A, B), footpath(B, C));
    (f(C, B), footpath(B, A));
    motorway(A, C).
    
   
    

route(A, B):-
    footpath(A, B);
    motorway(A, B).

/*Route starting from 52 to the building with larger number*/
f(52, 53).
f(52, 54).
f(52, 57).
f(52, 59).
f(52, 60).
f(52, 62).
f(52, 68).
f(52, 83).

/*Route starting from 53 to the building with larger number*/
m(53, 54).
f(53, 54).
f(53, 57).
m(53, 62).
m(53, 68).
m(53, 83).

/*Route starting from 54 to the building with larger number*/
m(54, 56).
f(54, 57).
m(54, 62).
m(54, 68).
m(63, 64).
m(54, 83).

/*Route starting from 56 to the building with larger number*/
f(56, 57).
f(56, 58).

/*Route starting from 57 to the building with larger number*/
f(57, 58).
f(57, 59).
f(57,62).
f(57,68).
f(57,83).

/*Route starting from 58 to the building with larger number*/
f(58, 59).

/*Route starting from 59 to the building with larger number*/
f(59, 60).
m(59, 60).
m(59, 62).
m(59, 68).
m(59, 83).

/*Route starting from 60 to the building with larger number*/
m(60, 62).
m(60, 68).
m(60, 83).

/*Route starting from 62 to the building with larger number*/
m(62, 63).
m(62, 64).
m(62, 66).
m(62, 68).
m(62, 76).
m(62, 83).


/*Route starting from 63 to the building with larger number*/
m(63, 64).
m(63, 66).
m(63, 68).
m(63, 69).
m(63, 76).

/*Route starting from 64 to the building with larger number*/
m(64, 66).
m(64, 68).
m(64, 69).
m(64, 76).

/*Route starting from 65 to the building with larger number*/
f(65, 58).
f(65, 83).

/*Route starting from 66 to the building with larger number*/
m(66, 68).
m(66, 69).
m(66, 76).

/*Route starting from 68 to the building with larger number*/
m(68, 76).
m(68, 83).

/*Route starting from 69 to the building with larger number*/
f(69, 76).



/*(b)*/
/*Test route*/
/*
?- route(56,57). 
true 
?- route(52,58). 
true 
?- route(54,52). 
true 
?- route(62,52). 
true 
?- route(52,11). 
true
*/
/*Test motorway*/
/*
?- motorway(56,57). 
false
?- motorway(52,58). 
false
?- motorway(62,53). 
true
?- motorway(53,54). 
true
?- motorway(52,11). 
false
*/

/*Test footpath*/
/*
?- footpath(56,57). 
true
?- footpath(52,58). 
true
?- footpath(54,52). 
true
?- footpath(62,52). 
true
?- footpath(52,11). 
false
*/

/*(c)*/
/*
Is there a motorway that connects Snell engineering Centre (58) to Snell Library (59)?  
?- motorway(52,58). 

A highway from 56 to 57 is desirable if it has only footpath connections?
?- footpath(56,58). 

which halls can be connected to hall 56 by motorway?
?- motorway(Hall, 56).

Is there a route between hall 52 and 68?
?- route(52, 68).

Can I walk from hall 60 to 54?
?- footpath(60, 52).

Is there a way to get hall 60 from 54?
?- route(54, 60).
*/