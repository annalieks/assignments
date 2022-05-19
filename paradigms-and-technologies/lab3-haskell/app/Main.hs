module Main where

import Debug.Trace

data Queue a = Queue [a] deriving (Show)

push :: a -> Queue a -> Queue a
push e (Queue es) = Queue (es ++ [e])

-- Precondition: Queue is not empty
pop :: Queue a -> (a, Queue a)
pop (Queue xs) = (head xs, Queue $ tail xs)

-- Precondition: Queue is not empty
peek :: Queue a -> a
peek (Queue (x:xs)) = x

empty :: Queue a -> Bool
empty (Queue []) = True
empty _ = False

findNeighbours :: Int -> [(Int, Int, Char)] -> [(Int, Int, Char)]
findNeighbours x automata = filter (\(y,_,_) -> y == x) automata

split :: Int -> String -> [String]
split len xs
  | len >= length xs = [xs]
  | otherwise = take len xs : split len (drop len xs)

checkEqual (x:y:z:_) | x == y && y == z = True
				     | otherwise = False
					
divide x = x `div` 3

checkWord w | mod (length w) 3 /= 0 = False
			| otherwise = checkEqual(split (divide(length w)) w)

listLast :: [String] -> String
listLast [x] = x
listLast (_:xs) = listLast xs
listLast [] = ""

getWord :: String -> String
getWord w | checkWord w = listLast (split (divide(length w)) w)
		  | otherwise = ""

isFinal b end | b == end = True
					  | otherwise = False

pushNeighbours :: [(Int, Int, Char)] -> Int -> Queue (Int, Int, Char) -> Queue (Int, Int, Char)
pushNeighbours aut b q = forEachNeighbour (findNeighbours b aut) q

forEachNeighbour :: [(Int, Int, Char)] -> Queue (Int, Int, Char) ->  Queue (Int, Int, Char)
forEachNeighbour [] q = q
forEachNeighbour (elem:[]) q = push elem q
forEachNeighbour (n:ns) q = forEachNeighbour ns (push n q)

addSymbol :: String -> Char -> String
addSymbol w c = w ++ [c]

checkCondition :: Int -> Char -> String -> Int -> Bool
checkCondition b c w end = if ((isFinal b end) && checkWord (addSymbol w c)) then True else False

finishIfPossible :: [(Int, Int, Char)] -> String -> ((Int, Int, Char), Queue (Int, Int, Char)) -> Int -> String
finishIfPossible aut w ((a, b, c), q) end | checkCondition a c w end = getWord (addSymbol w c)
								   | otherwise = searchPatterns aut (pushNeighbours aut b q) (addSymbol w c) end 

findWord :: [(Int, Int, Char)] -> Int -> Int -> String 
findWord (elem:aut) start end = searchPatterns ([elem] ++ aut) (push elem (Queue [])) "" end

searchPatterns :: [(Int, Int, Char)] -> Queue (Int, Int, Char) -> String -> Int -> String
-- automata, queue, current word, current vertex, end vertex
searchPatterns aut q w e | ((length w) > 1000 || empty q) = getWord w 
	| otherwise = finishIfPossible aut w (pop q) e
	-- | x == e = map (\(_,_,_) -> push (_, _, _) q) (findNeighbours x aut)

main :: IO ()
main = do
	print("Negative cases:")
	-- print(findWord [(1, 2, 'a')] 1 2)
	-- print(findWord [(1, 2, 'a'), (2, 3, 'a')] 1 3)
	-- print(findWord [(1, 2, 'a'), (2, 3, 'b'), (3, 4, 'c'), (3, 1, 'a')] 1 4)

	print("Positive cases:")
	-- print(findWord [(1, 2, 'a'), (2, 3, 'a'), (3, 1, 'c')] 1 3)
	-- print(findWord [(1, 2, 'a'), (2, 3, 'a'), (3, 4, 'a')] 1 4)
	-- print(findWord [(1, 2, 'a'), (2, 1, 'b')] 1 2)
	print(findWord [(1, 2, 'a'), (2, 3, 'b'), (3, 4, 'c'), (4, 2, 'a')] 1 4)
