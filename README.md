snake-clj
=========

An event sourced snake game written in clojure for Ludum Dare #31

REPL
====

Launch the game :

        (-main)

Handle runtime exceptions :

        (in-ns 'snake-clj.opengl.core)
        (set-screen-wrapper! (fn [screen screen-fn]
                               (try (screen-fn)
                                 (catch Exception e
                                   (.printStackTrace e)
                                   (Thread/sleep 10000)))))

Reload the screen :

        (on-gl (set-screen! snake-clj-game main-screen))



Licence
=======

Public Domain
