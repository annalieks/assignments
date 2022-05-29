visited([X,Y,Z], Visited) :-
  member([X,Y,Z,S], Visited).

%% Stack-based dfs
dfs([], E, S2, S3, Visited, F, ANS) :-
  member(X, F),
  member([S2,S3,X,ANS], Visited).


%% Skip elements that are already visited
dfs([[X,Y,Z,S]|T], E, S2, S3, Visited, F, ANS) :-
    visited([X,Y,Z],Visited),
    dfs(T, E, S2, S3, Visited, F, ANS).

movable([X,Y,Z], [X1,Y1,Z1,C], E) :-
  member([X,X1,C], E),
  member([Y,Y1,C], E),
  member([Z,Z1,C], E).

append_to_elem(S, [X,Y,Z,C], [X1,Y1,Z1,S2]) :- 
  atom_concat(S,C,S2),
  X = X1,
  Y = Y1,
  Z = Z1.

append_to_elem2(S, [C], Y) :- 
  atom_concat(S,C,Y).

dfs([[X,Y,Z,S]|T], E, S2, S3, Visited, F, ANS) :-
  not(visited([X,Y,Z],Visited)),
  % print([X,Y,Z,S]),
  % print('\n'),
  findall([X1,Y1,Z1,C], movable([X,Y,Z],[X1,Y1,Z1,C], E), Tos),
  maplist(append_to_elem(S), Tos, AP),
  append(AP, T, ToVisit),
  dfs(ToVisit, E, S2, S3, [[X,Y,Z,S]|Visited], F, ANS).


find_triples(Q, S2, S3, N, E, F) :-
  S2 =< N,
  S3 =< N,
  dfs([[Q,S2,S3,'']],E,S2,S3,[],F,ANS),
  print(ANS),
  print("\n").

find_triples(Q, S2, S3, N, E, F) :-
  S2 =< N,
  S3 < N,
  Y is S3+1,
  find_triples(Q, S2, Y, N, E, F).

find_triples(Q, S2, S3, N, E, F) :-
  S2 < N,
  S3 = N,
  Y is S2+1,
  find_triples(Q, Y, 1, N, E, F).

find_word(Q, N, E, F) :-
  find_triples(Q,1,1,N,E,F).


/* ------------ Unit tests ------------ */
:- begin_tests(test_automata).
test('Negative 1', all(Count = [1])) :- \+ find_word(1,2,[[1, 2, 'a']],[2]).
test('Negative 2', all(Count = [1])) :- \+ find_word(1,3, [[1, 2, 'a'], [2, 3, 'a']],[3]).
test('Negative 3', all(Count = [1])) :- \+ find_word(1,4,[[1, 2, 'a'], [2, 3, 'b'], [3, 4, 'c'], [3, 1, 'a']],[4]).

test('Negative 4', all(Count = [1])) :- \+ find_word(1,4,[[1,2,'a'],[2,2,'b'],[1,3,'b'],[2,4,'b'],[2,4,'a'],[3,3,'a']],[4]).
%% Two solutions
test('a 1', all(Count = [1, 1])) :- find_word(1,4,[[1,2,'a'],[2,2,'b'],[1,3,'b'],[2,4,'b'],[2,4,'a'],[3,3,'a'],[4,4,'a'],[4,4,'b']],[4]).
%% Four solutions
test('abab', all(Count = [1, 1, 1, 1])) :- find_word(1,4,[[1,2,'a'],[2,2,'b'],[1,3,'b'],[2,4,'b'],[2,4,'a'],[3,3,'a'],[4,4,'b'],[4,2,'a']],[4]).

test('ac', all(Count = [1])) :- find_word(1,3,[[1,2,'a'],[2,3,'c'],[3,2,'a']],[3]).
test('a 2', all(Count = [1])) :- find_word(1,4,[[1,2,'a'],[2,3,'a'],[3,4,'a']],[4]).
test('cbcb', all(Count = [1])) :- find_word(1,8,[[1,2,'c'],[2,3,'b'],[3,4,'c'],[4,5,'b'],[5,6,'c'],[6,7,'b'],[7,8,'c'],[8,1,'b']],[5]).
test('abc', all(Count = [1])) :- find_word(1,4,[[1,2,'a'],[2,3,'b'],[3,4,'c'],[4,2,'a']],[4]).

:- end_tests(test_automata).