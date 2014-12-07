(ns snake-clj.opengl.core
  (:import (java.util UUID)
           (com.badlogic.gdx.utils.viewport FitViewport))
  (:require [snake-clj.commands :as cmd]
            [snake-clj.matrix :as matrix]
            [snake-clj.core :as c]
            [snake-clj.db :as db]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]))

(declare snake-clj-game main-screen)
(def game-id 21)
(def seed 42)

(defn apple-texture [] (texture "apple.png"))
(def mem-apple-texture (memoize apple-texture))

(defn snake-texture [] (texture "snake.png"))
(def mem-snake-texture (memoize snake-texture))

(defn snake-head-up-texture [heading]
  (texture (str "snake-head-" (name heading) ".png")))
(def mem-snake-head-up-texture (memoize snake-head-up-texture))

(defn wall-texture [] (texture "wall.png"))
(def mem-wall-texture (memoize wall-texture))

(defn wx->screen
  "Convert world position to screen position"
  [n]
  (* 4 n))

(defn wy->screen
  "Convert world position to screen position"
  [height n]
  (* 4 (Math/abs (- n height))))

(defn apple-entities [height world]
  (for [x (range (matrix/arity-x world))
        y (range (matrix/arity-y world))
        :when (c/apple? (matrix/get-at world [x y]))]
    (assoc (mem-apple-texture)
           :x (wx->screen x)
           :y (wy->screen height y))))

(defn snake-head-entity [height [head-x head-y] heading]
  (assoc (mem-snake-head-up-texture heading)
         :x (wx->screen head-x)
         :y (wy->screen height head-y)))

(defn snake-entities [height snake heading]
  (into [(snake-head-entity height (c/head-of snake) heading)]
        (for [snake-bit (c/tail-of snake)
              :let [[snake-x snake-y] snake-bit]]
          (assoc (mem-snake-texture)
                 :x (wx->screen snake-x)
                 :y (wy->screen height snake-y)))))

(defn wall-entities [height world]
  (for [x (range (matrix/arity-x world))
        y (range (matrix/arity-y world))
        :when (c/wall? (matrix/get-at world [x y]))]
    (assoc (mem-wall-texture)
           :x (wx->screen x)
           :y (wy->screen height y))))

(defn update-entities []
  (let [{:keys [world snake alive? heading]} (db/load-aggregate game-id)
        height (matrix/arity-y world)]
    (if alive?
      (-> (into [] (apple-entities height world))
          (into (snake-entities height snake heading))
          (into (wall-entities height world)))
      [(label "DEAD" (color :red))])))

;; Original nokia is 84 x 48 pixels, but we will use virtual pixels of 4 pixels
(def w (* 84 4))
(def h (* 48 4))

(defn stage-fit-vp []
  (stage :set-viewport (FitViewport. w h)))

(defn init-graphic-settings [screen]
  (update! screen
           :camera (orthographic)
           :renderer (stage-fit-vp)))

(defn screen-wrapper []
  (set-screen-wrapper! (fn [screen screen-fn]
                         (try (screen-fn)
                              (catch Exception e
                                (.printStackTrace e)
                                (Thread/sleep 10000))))))

(defscreen main-screen
           :on-show
           (fn [screen _]
             (init-graphic-settings screen)
             (db/delete! game-id)
             (cmd/start-game! game-id seed)
             (update-entities))

           :on-resize
           (fn [screen _]
             (size! screen w h))

           :on-render
           (fn [screen entities]
             (clear!)
             (render! screen entities)
             (update-entities))

           :on-key-down
           (fn [screen entities]
             (condp = (:key screen)
               (key-code :escape) (do (db/delete! game-id)
                                      (cmd/start-game! game-id seed))
               (key-code :f) (cmd/go-ahead! game-id)
               (key-code :k) (cmd/turn-right! game-id)
               (key-code :j) (cmd/turn-left! game-id)
               nil)
             entities))

(defgame snake-clj-game
         :on-create
         (fn [this]
           (set-screen! this main-screen)))
