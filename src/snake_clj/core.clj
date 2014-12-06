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
  (let [[[[x-neck y-neck] [x-head y-head :as head]]] (drop (- (count snake) 2) snake)
        dir-x (- x-head x-neck)
        dir-y (- y-head y-neck)
        heading (if (zero? dir-x)
                  (if (pos? dir-x) :right :left)
                  (if (pos? dir-y) :bottom :top))]
    [head heading]))

(defn- move-right [world [x y] heading]
  (let [[xr yr] (condp = heading
                  :right [0 1]
                  :left [0 -1]
                  :up [1 0]
                  :bottom [-1 0])
        y (mod (+ y yr) (arity-y world))
        x (mod (+ x xr) (arity-x world))]
    [x y]))

(defn- is-tail? [snake p]
  (some #(= p %) snake))

(defn right-of [{world :world snake :snake}]
  (if world
    (let [[head-position heading] (head snake)
          new-head-position (move-right world head-position heading)]
      (prn snake new-head-position world)
      (if (is-tail? snake new-head-position)
        :tail
        (get-at world new-head-position)))
    nil))

(defn left-of [])

(defn ahead-of [])

(defn apple? [cell])

(defn wall? [cell])

(defn tail? [cell])