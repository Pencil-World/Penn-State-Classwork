# -----------------------------------------------------------------------
# SP24 CMPSC 360 Extra Credit Assignment 2
# RSA Implementation
# 
# Name: Ian Yee
# ID: 9-7301-3486
# 
# 
# You cannot use any external/built-in libraries to help compute gcd
# or modular inverse. You cannot use RSA, cryptography, or similar libs
# for this assignment. You must write your own implementation for generating
# large primes.
# 
# You are allowed to use random.randint() from the built-in random library
# -----------------------------------------------------------------------

from typing import Tuple
import random

# Type defs
Key = Tuple[int, int]

# Helper functions

def gcd(a: int, b: int) -> int:
    while b:
        a, b = b, a % b
    return a

def is_prime(n: int) -> bool:
    if n <= 1 or n % 2 == 0 or n % 3 == 0:
        return False
    if n <= 3:
        return True
    
    for _ in range(10):
        a = random.randint(2, n - 1)
        if pow(a, n - 1, n) != 1:
            return False
    
    return True

def modular_inverse(a: int, m: int) -> int:
    m0, x0, x1 = m, 0, 1
    while a > 1:
        q = a // m
        m, a = a % m, m
        x0, x1 = x1 - q * x0, x0
    return x1 + m0 if x1 < 0 else x1

# Project functions

def generate_prime(n: int) -> int:
    '''
    Description: Generate an n-bit prime number
    Args: n (No. of bits)
    Returns: prime number
    
    NOTE: This needs to be sufficiently fast or you may not get
    any credit even if you correctly return a prime number.
    '''
    while True:
        prime_candidate = random.getrandbits(n)
        if is_prime(prime_candidate):
            return prime_candidate

def generate_keypair(p: int, q: int) -> Tuple[Key, Key]:
    '''
    Description: Generates the public and private key pair
    if p and q are distinct primes. Otherwise, raise a value error
    
    Args: p, q (input integers)

    Returns: Keypair in the form of (Pub Key, Private Key)
    PubKey = (n,e) and Private Key = (n,d)
    '''
    if p == q:
        raise ValueError("p and q must be distinct primes")
    n = p * q
    phi_n = (p - 1) * (q - 1)

    e = generate_public_exponent(phi_n)
    d = modular_inverse(e, phi_n)

    return ((n, e), (n, d))

def generate_public_exponent(k: int) -> int:
    '''
    Description: Helper function that generates the SMALLEST
    public exponent for a given k value.

    Args: k (integer)

    Returns: e (public exponent) (e > 2)
    '''
    e = 3
    while gcd(e, k) != 1:
        e += 2
    return e

def rsa_encrypt(m: int, pub_key: Key) -> int:
    '''
    Description: Encrypts the message with the given public
    key using the RSA algorithm.

    Args: m (positive integer input)

    Returns: c (encrypted cipher)
    NOTE: You CANNOT use the pow function (or any similar function)
    here.
    '''
    n, e = pub_key
    return (m ** e) % n

def rsa_decrypt(c: int, priv_key: Key) -> int:
    '''
    Description: Decrypts the ciphertext using the private key
    according to RSA algorithm

    Args: c (encrypted cipher)

    Returns: m (decrypted message, an integer)
    NOTE: You CANNOT use the pow function (or any similar function)
    here.
    '''
    n, d = priv_key
    return (c ** d) % n

if __name__ == '__main__':
    pass