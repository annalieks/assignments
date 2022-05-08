:- style_check(-singleton).

freqs([], []).
freqs([X|Xs], [F|Fs]) :- count(Xs, X:1, F, X1), freqs(X1, Fs).

count([], F, F, []).
count([H|T], X:N, F, Fs) :- H=X, N1 is N+1, count(T, X:N1, F, Fs).
count([H|T], X:N, F, [H|Fs]) :- H\=X, count(T, X:N, F, Fs).

even(N):- mod(N,2) =:= 0.

is_even_count(X:N) :- even(N). 

filter(In, Out) :- exclude(is_even_count(), In, Out).

take_first([],[]).
take_first([X:N|T1],[X|T2]) :- take_first(T1,T2).

filter_odd_freq(L, R) :- freqs(L, T), filter(T, K), take_first(K, R).

/* ------------ Unit tests ------------ */
:- begin_tests(odd_frequencies).
test('All Odd', all(Count = [1])) :- filter_odd_freq([1, 2, 3], [1, 2, 3]).
test('One Odd', all(Count = [1])) :- filter_odd_freq([1, 2, 2], [1]).
test('Order Preserved', all(Count = [1])) :- filter_odd_freq([3, 1, 2], [3, 1, 2]).
test('All Even', all(Count = [1])) :- filter_odd_freq([3, 3, 1, 1], []).
test('Half Odd, Half Even', all(Count = [1])) :- filter_odd_freq([1, 1, 2, 2, 2], [2]).
test('One Even', all(Count = [1])) :- filter_odd_freq([1, 1, 1, 3, 3, 2, 2, 2], [1, 2]).
:- end_tests(odd_frequencies).