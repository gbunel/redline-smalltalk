" Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution "

TestCase < #BehaviorTest
  instanceVariableNames: ''.

- test
    self testInheritsFrom.

- testInheritsFrom
    | obj |
    obj := Child new.
    self assertTrue: (obj isKindOf: Child) withMessage: 'An instance of Child should be of kind Child'.
    self assertTrue: (obj isKindOf: Parent) withMessage: 'An instance of Child should be of kind Parent too'.
    " self assertTrue: (obj isKindOf: Object) withMessage: 'An instance of Child should be of kind Object too'. "
    self assertTrue: (Parent selectors size == 0) withMessage: 'Parent should implement no selector'.
    Transcript cr; show: TestCase selectors; cr.
    self assertTrue: (TestCase selectors size > 0) withMessage: 'TestCase should have method selectors'
