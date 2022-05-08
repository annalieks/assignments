:- style_check(-singleton).

min_elem([], M, M).
min_elem([H|T], X, M) :- H < X, min_elem(T, H, M).
min_elem([H|T], X, M) :- H >= X, min_elem(T, X, M).
min_elem([H|T], M) :- min_elem(T, H, M).

delete(X, [X|T], T) :- !.
delete(X, [Y|T], [Y|T1]) :- delete(X, T, T1).

delete_n_smallest(L, 0, L).
delete_n_smallest([], _, []).
delete_n_smallest(L, N, R) :- 
	N > 0,
	N1 is N - 1,
	min_elem(L, M),	
	delete(M, L, NewL),
	delete_n_smallest(NewL, N1, R).

/* ------------ Unit tests ------------ */
:- begin_tests(n_smallest).
test('Delete 1', all(Count = [1])) :- delete_n_smallest([1, 2, 3], 1, [2, 3]).
test('Delete 1 not in order', all(Count = [1])) :- delete_n_smallest([3, 1, 2], 1, [3, 2]).
test('Delete 2', all(Count = [1])) :- delete_n_smallest([1, 1, 1, 2], 2, [1, 2]).
test('Delete 2 not in order', all(Count = [1])) :- delete_n_smallest([4, 2, 5, 2], 2, [4, 5]).
test('Delete n from equal', all(Count = [1])) :- delete_n_smallest([4, 4, 4, 4], 3, [4]).
test('Delete n from non-equal', all(Count = [1])) :- delete_n_smallest([5, 2, 4, 6, 2, 1, 2], 3, [5, 4, 6, 2]).
test('Delete more than length', all(Count = [1])) :- delete_n_smallest([1], 2, []).
:- end_tests(n_smallest).