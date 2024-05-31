(ns generative-design.P-1-1-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;DESCRIPTION: A grid of rectangles is drawn. The color of each rectangle is determined by its position.

(def width 800)
(def height 400)
(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb width height 100)
  (q/no-stroke))

(defn draw-state [state]
  (let [stepX (+ (q/mouse-x) 2)
        stepY (+ (q/mouse-y) 2)
        gridX (range 0 width stepX)
        gridY (range 0 height stepY)
        gridXY (for [x gridX y gridY] [x y])]
    (doseq [[x y] gridXY]
      (q/fill x (- height y) 100)
      (q/rect x y stepX stepY))))

(defn key-pressed [state {:keys [key key-code]}]
  (when (or (= key :s) (= key :S))
    (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_1_1_01.jpg")))

(q/defsketch sketch
  :title "P-1-1-1-01"
  :size [width height]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed)