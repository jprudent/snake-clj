(ns snake-clj.core)

(defn- arity-y [matrix]
  (count matrix))

(defn- arity-x [matrix]
  (count (nth matrix 0)))

(defn- get-at [matrix [x y]]
  (-> matrix
      (nth y)
      (nth x)))

(defn- head [snake]
  (peek snake))

(defn- move-direction [world [x y] heading transformation-vector]
  (let [[xt yt] (heading transformation-vector)
        y (mod (+ y yt) (arity-y world))
        x (mod (+ x xt) (arity-x world))]
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

(defn- move-ahead [world head heading]
  (move-direction world head heading {:right [1 0]
                                      :left  [-1 0]
                                      :up    [0 -1]
                                      :down  [0 1]}))

(defn- is-tail? [snake p]
  (some #(= p %) snake))

(defn- direction-of [{:keys [world snake heading]} move-direction]
  (let [head (head snake)
        new-head-position (move-direction world head heading)]
    (if (is-tail? snake new-head-position)
      :tail
      (get-at world new-head-position))))

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

(defn apple? [cell])

(defn wall? [cell])

(defn tail? [cell])

(defn snake-state
  "factory method that returns a snake state"
  [world snake heading]
  {:pre [(> (arity-x world) 0)
         (> (arity-y world) 0)
         (vector? snake)
         (#{:up :down :right :left} heading)]}
  {:world world :snake snake :heading heading})