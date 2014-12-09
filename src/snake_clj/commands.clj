(ns snake-clj.commands
  (:require
    [snake-clj.core :refer :all]
    [snake-clj.events :as evt]
    [snake-clj.db :as db]
    [snake-clj.matrix :as matrix]))

(defn start-game! [id seed]
  (db/store! (evt/game-started id seed)))

(defn go-ahead! [id]
  (apply db/store!
         (if-let [{:keys [alive?]} (db/load-aggregate id)]
           (if alive?
             (let [turned-evt (evt/gone-ahead id)
                   snake-ahead (db/load-aggregate id turned-evt)
                   _ (println snake-ahead)
                   hit-smthing-evt (cond
                                     (wall? (what-is-on-head snake-ahead)) (evt/hit-wall id)
                                     (apple? (what-is-on-head snake-ahead)) (evt/ate-apple id)
                                     (tail? (what-is-on-head snake-ahead)) (evt/ate-tail id))]
               (if (nil? hit-smthing-evt)
                 [turned-evt]
                 [turned-evt hit-smthing-evt])))
           [])))

(defn turn-left! [id]
  (println "turn left")
  (if (:alive? (db/load-aggregate id))
    (do
      (apply db/store! [(evt/turned-left id)])
      (go-ahead! id))))

(defn turn-right! [id]
  (println "turn right")
  (if (:alive? (db/load-aggregate id))
    (do
      (apply db/store! [(evt/turned-right id)])
      (go-ahead! id))))