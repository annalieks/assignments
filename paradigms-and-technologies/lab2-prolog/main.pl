:- style_check(-singleton).

compose_sublist([L], [L], []).
compose_sublist([X,Y|Ys], Pr, Suff) :- (X > Y ->
        compose_sublist([Y|Ys], Pr0, Suff), Pr = [X|Pr0];
        Pr = [X], Suff = [Y|Ys]).

sorted_sublists([], []).
sorted_sublists(L, [Pr|T]) :-
    compose_sublist(L, Pr, Suff),
    sorted_sublists(Suff, T).

/* ------------ Unit tests ------------ */
:- begin_tests(split_to_sublists).
test('Split descreasing', all(Count = [1])) :- sorted_sublists([3, 2, 1], [[3, 2, 1]]).
test('Split increasing', all(Count = [1])) :- sorted_sublists([1, 2, 3], [[1], [2], [3]]).
test('Split equal', all(Count = [1])) :- sorted_sublists([3, 3, 2, 1], [[3], [3, 2, 1]]).
test('Split mixed', all(Count = [1])) :- sorted_sublists([1, 2, 3, 2, 3, 1], [[1], [2], [3, 2], [3, 1]]).
test('Split in 2', all(Count = [1])) :- sorted_sublists([3, 2, 1, 3, 2, 1], [[3, 2, 1], [3, 2, 1]]).
test('Split empty', all(Count = [1])) :- sorted_sublists([], []).
:- end_tests(split_to_sublists).