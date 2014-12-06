(ns snake-clj.events
  (:import (java.util Random))
  (:require [snake-clj.core :refer :all]
            [snake-clj.matrix :as matrix]
            [clojure.data.generators :as gen]))

(defn- event [type id]
  {:event-type type
   :id         id})

(defn game-started [id seed]
  (assoc
    (event :game-started id)
    :seed seed))

(defn hit-wall [id]
  (event :hit-wall id))

(defn ate-tail [id]
  (event :ate-tail id))

(defn ate-apple [id]
  (event :ate-apple id))

(defn turned-right [id]
  (event :turned-right id))

(defn turned-left [id]
  (event :turned-left id))

(defn gone-ahead [id]
  (event :gone-ahead id))

;;;;;;;;;;;;;;;;;;
;; Event handlers

(defmulti handle-event (fn [state event] (:event-type event)))

;; GAME STARTED

(defn- rand-cell []
  (gen/weighted {:apple      1
                 :wall       5
                 (fn [] nil) 20}))

(defn- rand-world []
  (partition 5 (for [_ (range 25)]
                 (rand-cell))))

(defn- rand-heading []
  (gen/one-of :up :down :right :left))

(defn- rand-position [matrix]
  (let [f (fn [] (gen/uniform 0 (matrix/arity-y matrix)))]
    (gen/tuple f f)))

(defmethod handle-event :game-started
  [state {seed :seed}]
  {:pre [(nil? state)]}
  (binding [gen/*rnd* (Random. seed)]
    (let [world (rand-world)]
      (snake-state world (rand-position world) (rand-heading) seed))))

;; HIT WALL

(defn- kill
  "This method can kill snakes bare handed"
  [state]
  (assoc state :alive? false))

(defmethod handle-event :hit-wall
  [{:keys [world snake alive?] :as state} _]
  {:pre [alive?
         (wall? (matrix/get-at world (head-of snake)))]}
  (kill state))

;; EAT TAIL

(defmethod handle-event :ate-tail
  [{:keys [snake alive?] :as state} _]
  {:pre [alive?
         (is-tail? (tail-of snake) (head-of snake))]}
  (kill state))

;; ATE APPLE

(defn- free-cells [world snake]
  (for [x (range (matrix/arity-x world))
        y (range (matrix/arity-y world))
        :let [p [x y]]
        :when (and (not (is-tail? snake p))
                   (nil? (matrix/get-at world p)))]
    p))

(defn- somewhere-free [world snake seed]
  (binding [gen/*rnd* (Random. seed)]
    (apply gen/one-of (free-cells world snake))))

(defn- reverse-heading [heading]
  (heading {:right :left, :left :right, :up :down, :down :up}))

(defn- neck-heading [world head neck]
  (first
    (for [heading [:up :down :left :right]
          :when (= head (move-ahead world neck heading))]
      heading)))

(defn- tail-heading
  "compute the heading of tail"
  [world snake heading]
  (if (>= 2 (count snake))
    (neck-heading world (second snake) (first snake))
    heading))

(defn- grow-tail [world snake heading]
  (let [tailest (first snake)
        rev-tail-heading (reverse-heading (tail-heading world snake heading))
        new-tailest (move-ahead world tailest rev-tail-heading)]
    (into [new-tailest] snake)))

(defmethod handle-event :ate-apple
  [{:keys [world snake alive? heading seed] :as state} _]
  {:pre [alive?
         (integer? seed)
         (apple? (matrix/get-at world (head-of snake)))]}
  (assoc state
         :world (-> world
                    (matrix/set-at (head-of snake) nil)
                    (matrix/set-at (somewhere-free world snake seed) :apple))
         :snake (grow-tail world snake heading)))

;; Turned right

(defn right-heading-of [heading]
  (heading {:down  :left,
            :left  :up
            :up    :right
            :right :down}))

(defmethod handle-event :turned-right
  [{:keys [heading alive?] :as state} _]
  {:pre [alive?]}
  (assoc state :heading (right-heading-of heading)))

;; Turned left

(defmethod handle-event :turned-left
  [{:keys [heading alive?] :as state} _]
  {:pre [alive?]}
  (assoc state :heading (reverse-heading (right-heading-of heading))))

;; Gone ahead

(defmethod handle-event :gone-ahead
  [{:keys [world heading snake alive?] :as state} _]
  {:pre [alive?]}
  (assoc state
         :snake (-> (conj snake (move-ahead world (head-of snake) heading))
                    (subvec 1))))