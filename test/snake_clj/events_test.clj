(ns snake-clj.events-test
  (:require [clojure.test :refer :all]
            [snake-clj.core :refer :all]
            [snake-clj.events :refer :all]
            [snake-clj.matrix :as matrix]))

(deftest test-game-started
  (let [event (game-started 12 42)
        {:keys [world snake heading seed]} (handle-event nil event)
        [snake-x snake-y] snake]
    (is (= 5 (matrix/arity-x world)))
    (is (= 5 (matrix/arity-y world)))
    (is (<= 0 snake-x 4))
    (is (<= 0 snake-y 4))
    (is (#{:up :down :right :left} heading))
    (is (= 42 seed)))
  (is (= (handle-event nil (game-started 1 42))
         (handle-event nil (game-started 9 42)))
      "2 games started with the same seed are equals"))

(deftest test-hit-wall
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]
        event (hit-wall 12)
        state (snake-state world [[2 0]] :up 42)
        {:keys [alive?]} (handle-event state event)]
    (is (not alive?) "When snake hits a wall he is not alive anymore")))

(deftest test-ate-tail
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]
        event (ate-tail 12)
        state (snake-state world [[1 1] [1 0] [0 0] [0 1] [1 1]] :right 42)
        {:keys [alive?]} (handle-event state event)]
    (is (not alive?) "When snake eats his tail he is not alive anymore")))

(defn find-apples [world]
  (for [x (range (matrix/arity-x world))
        y (range (matrix/arity-y world))
        :when (apple? (matrix/get-at world [x y]))]
    [x y]))

(deftest test-ate-apple
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]
        event (ate-apple 12)
        state (snake-state world [ [0 1] [0 2]] :down 42)
        {:keys [alive? world snake]} (handle-event state event)]
    (is alive?
        "An apple can't kill a snake")
    (is (= nil (matrix/get-at world (head-of snake)))
        "When snake ate the apple, it disappeared of the world")
    (is (= 1 (count (find-apples world)))
        "When snake ate the apple, another one appeared")
    (is (#{[0 0] [1 0] [1 1] [2 1] [1 2]} (first (find-apples world)))
        "Fortunatly, the apple that appeared is not on a wall nor on the snake")
    (is (and (= [0 0] (first snake))
             (= 3 (count snake)))
        "Apples are nutritive and made the snake tail grow")))