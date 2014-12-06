(ns snake-clj.events-test
  (:require [clojure.test :refer :all]
            [snake-clj.core :refer :all]
            [snake-clj.events :refer :all]
            [snake-clj.matrix :as matrix]))

(deftest test-game-started
  (let [event (game-started 12 42)
        {:keys [world snake heading]} (handle-event nil event)
        [snake-x snake-y] snake]
    (is (= 5 (matrix/arity-x world)))
    (is (= 5 (matrix/arity-y world)))
    (is (<= 0 snake-x 4))
    (is (<= 0 snake-y 4))
    (#{:up :down :right :left} heading))
  (is (= (handle-event nil (game-started 1 42))
         (handle-event nil (game-started 9 42)))
      "2 games started with the same seed are equals"))

(deftest test-hit-wall
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]
        event (hit-wall 12)
        state (snake-state world [[2 0]] :up)
        {:keys [alive?]} (handle-event state event)]
    (is (not alive?) "When snake hits a wall he is not alive anymore")))

(deftest test-ate-tail
  (let [world [[nil nil :wall]
               [nil nil nil]
               [:apple nil :wall]]
        event (ate-tail 12)
        state (snake-state world [[1 1] [1 0] [0 0] [0 1] [1 1]] :right)
        {:keys [alive?]} (handle-event state event)]
    (is (not alive?) "When snake eats his tail he is not alive anymore")))