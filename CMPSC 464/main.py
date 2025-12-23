from math import comb

# is variable
def _is_var(ch: str) -> bool:
  return len(ch) == 1 and "A" <= ch <= "Z"

# is terminal
def _is_term(ch: str) -> bool:
  return len(ch) == 1 and (("a" <= ch <= "z") or ("0" <= ch <= "9"))

"""
Reads the grammar file format:
  n
  LHS=alt1|alt2|...
  ...
"""
def _parse_grammar_file(filename: str):
  # reads file
  with open(f"CMPSC 464/{filename}", "r", encoding="utf-8") as f:
    rule_lines = [ln.strip() for ln in f.readlines() if ln.strip() != ""]
  n = int(rule_lines[0])
  del rule_lines[0]
  i = 1

  grammar = {}
  for line in rule_lines:
    line = line.replace(" ", "")
    lhs, rhs = line.split("=", 1)

    alts = rhs.split("|") if rhs != "" else [""]
    for alt in alts:
      if alt == "$":
        alt = ""  # epsilon
      grammar.setdefault(lhs, []).append(alt)

  return grammar

"""
CNF rules allowed:
  A -> BC   (two variables)
  A -> a  (one terminal)
  S -> ε   (only S can produce epsilon, represented by "")
  NO other variable can have an epsilon
"""
def function1_is_cnf(grammar_filename: str) -> bool:
  grammar = _parse_grammar_file(grammar_filename)

  # no start symbol; in cnf, there is always a start symbol
  if "S" not in grammar:
    return False

  has_eps = False

  for A, rhss in grammar.items():
    # must have lhs, lhs is a variable
    if len(A) != 1 or not _is_var(A):
      return False

    for rhs in rhss:
      # Validate characters
      for ch in rhs:
        if not (_is_var(ch) or _is_term(ch)):
          return False

      if rhs == "":
        # epsilon
        if A != "S":
          return False
        has_eps = True
        continue

      if len(rhs) == 1:
        # must be terminal
        if not _is_term(rhs):
          return False
      elif len(rhs) == 2:
        # must be two variables
        if not (_is_var(rhs[0]) and _is_var(rhs[1])):
          return False
      else:
        return False

  if has_eps:
    # Standard CNF condition when S -> ε is allowed:
    # S must not appear on any RHS
    for A, rhss in grammar.items():
      for rhs in rhss:
        if rhs != "" and "S" in rhs:
          return False

  return True

def _catalan(m: int) -> int:
  # Catalan(m) = (2m choose m) / (m+1)
  return comb(2 * m, m) // (m + 1)

"""
For n = len(w) >= 1, explore all derivations of exactly 2n-1 steps
All LEFTMOST derivations
"""
def function2_naive_membership(grammar_filename: str, w: str) -> bool:
  g = _parse_grammar_file(grammar_filename)

  n = len(w)
  if n == 0:
    return True if ("" in g.get("S", [])) else False

  # Depth limit
  limit = 2 * n - 1
  prods = g

  def dfs(form: str, steps_used: int) -> bool:
    if steps_used == limit:
      return form == w

    if len(form) > n:
      return False

    # Find leftmost variable
    pos = None
    for i, ch in enumerate(form):
      if _is_var(ch):
        pos = i
        break
    if pos is None:
      return False  # no variables left, but not at depth limit / not equal to w

    # Prefix terminals before the leftmost variable are already fixed in a leftmost derivation
    if form[:pos] != w[:pos]:
      return False

    A = form[pos]
    for rhs in prods.get(A, []):
      new_form = form[:pos] + rhs + form[pos + 1 :]
      if dfs(new_form, steps_used + 1):
        return True
    return False

  return True if dfs("S", 0) else False


"""
outputs True or False depending on if running function2 is
GUARANTEED (worst case) to finish within 1 minute for ANY string of that length.

Bmax = max number of binary productions (A->BC) for any variable
Tmax = max number of terminal productions (A->a) for any variable
"""
def function3_guaranteed_under_1_min(grammar_filename: str, string_length: int) -> str:
  g = _parse_grammar_file(grammar_filename)

  n = string_length
  if n == 0:
    return True

  Tmax = 0
  Bmax = 0
  for A, rhss in g.items():
    tcnt = 0
    bcnt = 0
    for rhs in rhss:
      if rhs == "":
        continue
      if len(rhs) == 1 and _is_term(rhs):
        tcnt += 1
      elif len(rhs) == 2 and _is_var(rhs[0]) and _is_var(rhs[1]):
        bcnt += 1
    Tmax = max(Tmax, tcnt)
    Bmax = max(Bmax, bcnt)

  if Tmax == 0:
    return True

  # Worst-case bound on #parse trees
  shapes = _catalan(n - 1)
  parse_tree_upper = shapes * (Bmax ** (n - 1)) * (Tmax ** n)

  steps_per_derivation = 2 * n - 1
  work_upper = parse_tree_upper * steps_per_derivation

  return True if work_upper <= 6_000_000 else False

def main():
  print("1. ", function1_is_cnf("grammarA.txt"))
  print("2. ", function1_is_cnf("grammarB.txt"))
  print("3. ", function1_is_cnf("grammarH.txt"))
  print("4. ", function1_is_cnf("grammarR.txt"))
  print("5. ", function1_is_cnf("grammarG.txt"))
  print("6. ", function3_guaranteed_under_1_min("grammarB.txt", 3))
  print("7. ", function3_guaranteed_under_1_min("grammarB.txt", 20))
  print("8. ", function3_guaranteed_under_1_min("grammarR.txt", 10_000))
  print("9. ", function3_guaranteed_under_1_min("grammarS.txt", 10_000))
  print("10. ", function3_guaranteed_under_1_min("grammarR.txt", 8))
  print("11. ", function2_naive_membership("grammarB.txt", ''))
  print("12. ", function2_naive_membership("grammarB.txt", 'bab'))
  print("13. ", function2_naive_membership("grammarS.txt", 'a'))
  print("14. ", function2_naive_membership("grammarS.txt", 'b'))
  print("15. ", function2_naive_membership("grammarR.txt", 'aaa'))
  print("16. ", function2_naive_membership("grammarR.txt", 'a'))
  print("17. ", function2_naive_membership("grammarG.txt", 'ccc'))
  print("18. ", function2_naive_membership("grammarG.txt", 'abacb'))
  print("19. ", function2_naive_membership("grammarG.txt", 'aacbb'))
  print("20. ", function2_naive_membership("grammarG.txt", 'acbcc'))

main()
