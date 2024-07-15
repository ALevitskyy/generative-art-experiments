(ns gd.P-1-2-3-02
  [:require [quil.core :as q]
   [quil.middleware :as m]])


(def colorCount 20)

(defn random-vector-len-n
  ([val n] (take n (repeatedly #(q/random val))))
  ([min max n] (take n (repeatedly #(q/random min max)))))
(defn constant-vector-len-n [val n] (vec (repeat n val)))
(defn interleave-2-vectors [v1 v2] (mapcat vector v1 v2))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke))

(defn generate-fragments [partCount]
  (let [mask (repeatedly partCount #(< (q/random 1) 0.075))
        generator #(q/random 2)]
    (mapcat #(if % (repeatedly (+ partCount (int (q/random 2 20))) generator) [(q/random 2 20)]) mask)))


(defn draw-state [state]
  (let [noLoop (q/no-loop)
        randomSeed (q/random-seed (q/random 100000))
        hueValues (interleave-2-vectors (random-vector-len-n 130 200 colorCount) (constant-vector-len-n 195 colorCount))
        saturationValues (interleave-2-vectors (constant-vector-len-n 100 colorCount) (random-vector-len-n 20 100 colorCount))
        brightnessValues (interleave-2-vectors (random-vector-len-n 15 100 colorCount) (constant-vector-len-n 100 colorCount))
        rowCount (int (q/random 5 30))
        rowHeight (/ (q/height) rowCount)
        partCount (map #(+ % 1) (range rowCount))
        fragments (map generate-fragments partCount)
        fragment-cumcount (reductions + (map #(count %) fragments))
        indexed-fragments (map vector (range rowCount) fragments fragment-cumcount)]
    (doseq [[i parts parts-cumcount] indexed-fragments]
      (let [sumPartsTotal (reduce + parts)
            sumPartsNow (reductions + 0 parts)
            x (map #(q/map-range % 0 sumPartsTotal 0 (q/width)) sumPartsNow)
            y (* i rowHeight)
            w (map #(q/map-range % 0 sumPartsTotal 0 (q/width))   parts)
            h rowHeight
            counter (range (- parts-cumcount (count parts)) parts-cumcount)
            index (map #(mod % colorCount)  counter)
            hue (map #(nth hueValues %) index)
            saturation (map #(nth saturationValues %) index)
            brightness (map #(nth brightnessValues %) index)
            params (map vector hue saturation brightness x w)]
        (doseq [[hu s b x w] params]
          (q/fill hu s b)
          (q/rect x y w h))))))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_02.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_02.jpg")))

(defn mouse-released [_ __]
  (q/start-loop))

(def sketch (atom nil))
(q/defsketch sketch
  :title "test"
  :size [800 800]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed
  :mouse-released mouse-released)

