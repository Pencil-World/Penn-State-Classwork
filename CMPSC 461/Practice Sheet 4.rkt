#lang racket/base
(display "Hello, World!")

(define (digits-to-number lst)
  (foldl (lambda (x acc) (+ x (* acc 10))) 0 lst)
)

(display "\ndigits-to-numbers\n")
(digits-to-number '(1 2 3 4)) ; Output: 1234

(define (reverse-foldl lst)
  (foldl cons '() lst)
)

(display "\nreverse-foldl\n")
(reverse-foldl '(1 2 3 4 5)) ; Output: (5 4 3 2 1)

(define (length-foldr lst)
  (foldr (lambda (x acc) (+ acc 1)) 0 lst)
)

(display "\nlength-foldr\n")
(length-foldr '(a b c d e)) ; Output: 5
(length-foldr '()) ; Output: 0

(define (all? pred lst)
  (foldr (lambda (x acc) (and (pred x) acc)) #t lst)
)

(display "\nall?\n")
(all? even? '(2 4 6 8)) ; Output: t
(all? even? '(2 4 7 8)) ; Output: f

(define (duplicate lst)
  (foldr (lambda (x acc) (append (list x x) acc)) '() lst)
)

(display "\nduplicate\n")
(duplicate '(1 2 3)) ; Output: (1 1 2 2 3 3)