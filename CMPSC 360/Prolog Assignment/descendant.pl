% Declare your facts here
father(john, jim).
father(john, lisa).
father(john, james).
father(bob, mike).
father(bob, ann).
father(tom, pat).
father(tom, sarah).
father(david, ike).
father(mark, sam).
father(mark, emma).
father(james, david).
father(james, olivia).

% Complete the following horn clause
descendant(X, Y) :-
    father(Y, X).

descendant(X, Y) :- 
    father(Y, Z),
    descendant(X, Z).

% Example
% ?- descendant(david, james). should return True