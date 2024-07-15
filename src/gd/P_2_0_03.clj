(ns gd.P-2-0-03
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def width 720)
(def TAU (* 2 Math/PI))
(def strokeColor (atom nil))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-fill)
  (q/background 360)
  (q/stroke-weight 2)
  (q/stroke 0 25)
  (reset! strokeColor (q/color 0 10)))

(defn draw-shape []
  (q/push-matrix)
  (q/translate (/ width 2) (/ width 2))
  (q/stroke @strokeColor)
  (q/begin-shape)
  (let [circle-resolution (int (q/map-range (+ (q/mouse-y) 100) 0 width 2 10))
        radius (- (q/mouse-x) (/ width 2))
        angle (/ TAU circle-resolution)]
    (doseq [i (range (+ circle-resolution 1))]
      (q/vertex
       (* radius (Math/cos (* i angle)))
       (* radius (Math/sin (* i angle))))))
  (q/end-shape)
  (q/pop-matrix))

(defn draw-state [state]
  (when (and (q/mouse-pressed?) (= (q/mouse-button) :left))
    (draw-shape)))


(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_2_0_03.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_2_0_03.jpg")
    :r (q/background 360)
    :backspace (q/background 360)
    :delete (q/background 360)
    : (q/background 360)
    :1 (reset! strokeColor (q/color 0 10))
    :2 (reset! strokeColor (q/color 192 100 64 10))
    :3 (reset! strokeColor (q/color 52 100 71 10))
    (println key)))

(def sketch (atom nil))
(defn run-sketch []
  (q/defsketch sketch
    :host "sketch-canvas"
    :title "P_2_0_03"
    :size [width width]
    :setup setup
    :draw draw-state
    :middleware [m/fun-mode]
    :key-pressed key-pressed))