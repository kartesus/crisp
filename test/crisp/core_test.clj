(ns crisp.core-test
  (:require [clojure.test :refer :all]
            [crisp.core :as sut]))

(deftest self-evaling
  (is (= "hello" (sut/crisp-eval {} "hello")))
  (is (= 1 (sut/crisp-eval {} 1)))
  (is (= nil (sut/crisp-eval {} nil))))

(deftest symbol-lookup
  (is (= 1 (sut/crisp-eval {'a 1} 'a)))
  (is (thrown? Exception (sut/crisp-eval {} 'a))))

(deftest special-forms
  (is (= 1 (sut/crisp-eval {} '(if true 1 2))))
  (is (= 2 (sut/crisp-eval {} '(if false 1 2))))

  (is (= 'a (sut/crisp-eval {} '(quote a))))

  (is (= 1 (sut/crisp-eval {} '(do 3 2 1))))

  (is (= 1 (sut/crisp-eval {} '(let [a 1] a))))
  (is (= 2 (sut/crisp-eval {} '(let [a 1 b 2] b))))
  (is (= 1 (sut/crisp-eval {'a 1} '(let [b 2] a))))
  (is (= 2 (sut/crisp-eval {'a 1} '(let [a 2] a))))
  (is (= 1 (sut/crisp-eval {} '(let [a 1 b a] b))))
