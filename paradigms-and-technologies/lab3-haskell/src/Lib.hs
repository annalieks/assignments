module Lib where

import Data.List

findOutcoming :: Int -> [(Int, Int, Char)] -> [(Int, Int, Char)]
findOutcoming st = filter (\(a,_,_) -> a == st)

findSameEdge :: Char -> [(Int, Int, Char)] -> [(Int, Int, Char)]
findSameEdge e = filter (\(_,_,a) -> a == e)

findByVertexAndEdge :: Int -> Char -> [(Int, Int, Char)] -> [(Int, Int, Char)]
findByVertexAndEdge v e = take 1 . filter (\(vx,_,ex) -> vx == v && ex == e)

pairs :: [a] -> [[a]]
pairs l = [[x,y] | (x:ys) <- tails l, y <- ys]

combinationsForPair :: a -> [[a]] -> [[a]]
combinationsForPair _ [] = []
combinationsForPair f (p:ps) = (f:p):combinationsForPair f ps

merge :: [a] -> [a] -> [a]
merge [] ys = ys
merge (x:xs) ys = x:merge ys xs

combinationsForEach :: [(Int, Int, Char)] -> [(Int, Int, Char)] -> [[(Int, Int, Char)]]
combinationsForEach [] _ = []
combinationsForEach ((st, en, e):xs) aut = merge (combinationsForPair (st, en, e) (pairs (findSameEdge e aut))) (combinationsForEach xs aut)

-- for each combination of 3 do dfs
combinations :: Int -> [(Int, Int, Char)] -> [[(Int, Int, Char)]]
combinations st aut = combinationsForEach (findOutcoming st aut) aut 

first :: (a, b, c) -> a
first (c, _, _) = c
  
second :: (a, b, c) -> b
second (_, c, _) = c
  
third :: (a, b, c) -> c
third (_, _, c) = c
  
intersection:: (Eq a) => [[a]] -> [a]
intersection = foldr1 intersect

listOfVertices :: Int -> Int -> Int -> [(Int, Int, Char)] -> [[(Int, Int, Char)]]
listOfVertices v1 v2 v3 aut = [
  findOutcoming v1 aut, 
  findOutcoming v2 aut,
  findOutcoming v3 aut]
  
getFirstElem :: [(Int, Int, Char)] -> (Int, Int, Char)
getFirstElem [] = (0, 0, '_')
getFirstElem (x:_) = x
  
listEdges :: [(a, b1, b2)] -> [b2]
listEdges = map third

listOfEdges :: [[(a, b1, b2)]] -> [[b2]]
listOfEdges = map listEdges 

getNextVertex :: Int -> Char -> [(Int, Int, Char)] -> Int
getNextVertex v e aut = second (getFirstElem (findByVertexAndEdge v e aut))

forEachEdge :: [Char] -> (Int, Int, Int) -> [(Int, Int, Char)] -> [(Int, Int, Int)] -> t -> Int -> String -> Int -> Int -> String
forEachEdge [] _ _ vis _ en w a2 a3 = getAnswer vis w a2 a3 en 
forEachEdge (e:edges) (v1, v2, v3) aut vis st en w a2 a3 = dfs 
 (getNextVertex v1 e aut, getNextVertex v2 e aut, getNextVertex v3 e aut) aut ((v1, v2, v3):vis) st en w a2 a3
  
nextIteration :: (Int, Int, Int) -> [(Int, Int, Char)] -> [(Int, Int, Int)] -> t -> Int -> String -> Int -> Int -> String
nextIteration (v1, v2, v3) aut = forEachEdge (intersection (listOfEdges (listOfVertices v1 v2 v3 aut))) (v1, v2, v3) aut
   
getAnswer :: (Eq t1, Eq t2, Eq t3) => [(t1, t2, t3)] -> String -> t1 -> t2 -> t3 -> String
getAnswer [] _ _ _ _ = ""
getAnswer ((v1, v2, v3):vis) w a2 a3 en = if (v1 == a2) && (v2 == a3) && (v3 == en) 
then w else getAnswer vis w a2 a3 en

processTriples :: [[(Int, Int, Char)]] -> [(Int, Int, Char)] -> Int -> Int -> String
processTriples [] _ _ _ = ""
processTriples (v:_) aut st en = processTriple v aut st en

processTriple :: [(Int, Int, Char)] -> [(Int, Int, Char)] -> Int -> Int -> String
processTriple _ [] _ _ = ""
processTriple [] _ _ _ = ""
processTriple [_] _ _ _ = ""
processTriple [_, _] _ _ _ = ""
processTriple (v1:v2:v3:_) aut st en = dfs (second v1, second v2, second v3) aut [] st en [third v1] (first v2) (first v3)
  
dfs :: (Int, Int, Int) -> [(Int, Int, Char)] -> [(Int, Int, Int)] -> t -> Int -> String -> Int -> Int -> String
dfs (v1, v2, v3) aut vis st en w a2 a3 =
  if (v1, v2, v3) `elem` vis then getAnswer vis w a2 a3 en
  else nextIteration (v1,v2,v3) aut vis st en w a2 a3

findWord :: [(Int, Int, Char)] -> Int -> Int -> String
findWord aut st = processTriples (combinations st aut) aut st


main :: IO ()
main = do
	print("Negative cases:")
	print(findWord [(1, 2, 'a')] 1 2)
	print(findWord [(1, 2, 'a'), (2, 3, 'a')] 1 3)
	print(findWord [(1, 2, 'a'), (2, 3, 'b'), (3, 4, 'c'), (3, 1, 'a')] 1 4)

	print("Positive cases:")
	print(findWord [(1, 2, 'a'), (2, 3, 'c'), (3, 2, 'a')] 1 3)
	print(findWord [(1, 2, 'a'), (2, 3, 'a'), (3, 4, 'a')] 1 4)
	print(findWord [(1, 2, 'c'), (2, 3, 'b'), (3, 4, 'c'), (4, 5, 'b'), 
					(5, 6, 'c'), (6, 7, 'b'), (7, 8, 'c'), (8, 1, 'b')] 1 5)
	print(findWord [(1, 2, 'a'), (2, 3, 'b'), (3, 4, 'c'), (4, 2, 'a')] 1 4)

	print("Predefined")
	print(findWord [(0, 1, 'a'), (1, 1, 'b'), (1, 3, 'a'), (0, 2, 'b'), (2, 2, 'a'), (2, 3, 'b')] 1 3)
	print(findWord [(0, 1, 'a'), (1, 1, 'b'), (1, 3, 'a'), (0, 2, 'b'), (2, 2, 'a'), (2, 3, 'b'), (3, 3, 'a'), (3, 3, 'b')] 1 3)
	print(findWord [(0, 1, 'a'), (1, 1, 'b'), (1, 3, 'a'), (0, 2, 'b'), (2, 2, 'a'), (2, 3, 'b'), (3, 1, 'a'), (3, 3, 'b')] 1 3)
	