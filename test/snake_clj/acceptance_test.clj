(ns snake-clj.acceptance-test
  (:require [clojure.test :refer :all]
            [snake-clj.core :refer :all]
            [snake-clj.commands :as cmd]
            [snake-clj.db :as db]))

(deftest test-1
  (do
    (cmd/start-game! 1 42)
    (let [state (db/load-aggregate 1)]
      (is (vector? (:world state))))))

