(ns snake-clj.core
  (:require [snake-clj.matrix :as matrix]))

(defn head-of [snake]
  (peek snake))

(defn tail-of [snake]
  (into [] (butlast snake)))

(defn- move-direction [world [x y] heading transformation-vector]
  (let [[xt yt] (heading transformation-vector)
        y (mod (+ y yt) (matrix/arity-y world))
        x (mod (+ x xt) (matrix/arity-x world))]
    [x y]))

(defn- move-right [world head heading]
  (move-direction world head heading {:right [0 1]
                                      :left  [0 -1]
                                      :up    [1 0]
                                      :down  [-1 0]}))

(defn- move-left [world head heading]
  (move-direction world head heading {:right [0 -1]
                                      :left  [0 1]
                                      :up    [-1 0]
                                      :down  [1 0]}))

(defn move-ahead [world head heading]
  (move-direction world head heading {:right [1 0]
                                      :left  [-1 0]
                                      :up    [0 -1]
                                      :down  [0 1]}))

(defn is-tail? [tail p]
  (some #(= p %) tail))

(defn- direction-of [{:keys [world snake heading]} move-direction]
  (let [head (head-of snake)
        new-head-position (move-direction world head heading)]
    (if (is-tail? snake new-head-position)
      :tail
      (matrix/get-at world new-head-position))))

(defn right-of
  "Peek what's on right"
  [snake-state]
  (direction-of snake-state move-right))

(defn left-of
  "Peek what's on left"
  [snake-state]
  (direction-of snake-state move-left))

(defn ahead-of
  "Peek what's ahead"
  [snake-state]
  (direction-of snake-state move-ahead))

(defn apple? [cell] (= :apple cell))

(defn wall? [cell] (= :wall cell))

(defn tail? [cell] (= :tail cell))

(defn snake-state
  "factory method that returns a snake state"
  [world snake heading seed]
  {:pre [(> (matrix/arity-x world) 0)
         (> (matrix/arity-y world) 0)
         (vector? snake)
         (#{:up :down :right :left} heading)]}
  {:world world :snake snake :heading heading :alive? true :seed seed})