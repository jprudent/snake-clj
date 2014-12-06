(ns snake-clj.events-test
  (:require [clojure.test :refer :all]
            [snake-clj.events :refer :all]
            [snake-clj.matrix :as matrix]))

(deftest test-game-started
  (let [event (game-started 12 42)
        {:keys [world snake heading]} (handle-event event)
        [snake-x snake-y] snake]
    (is (= 5 (matrix/arity-x world)))
    (is (= 5 (matrix/arity-y world)))
    (is (<= 0 snake-x 4))
    (is (<= 0 snake-y 4))
    (#{:up :down :right :left} heading))
  (is (= (handle-event (game-started 1 42))
         (handle-event (game-started 9 42)))
      "2 games started with the same seed are equals"))