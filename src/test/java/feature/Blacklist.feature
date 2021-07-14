Feature: Blacklist

  @blacklist @bl_auth
  Scenario Outline: A Fraud analyst with a valid auth, adds blacklisted info to the Company blacklist
    Given I am a fraud analyst with auth token '<token>'
    When I add '<userinfotype>', '<info>' in the company blacklist
    Then Item is succefully created

    Examples: 
      | token         | userinfotype | info            | code     |
      | retF34$$evbzZ | email        | obama@gmail.com | bl_email |
      | retF34$$evbzZ | name         | Barak Obama     | bl_name  |
      | retF34$$evbzZ | phone        |      3104211111 | bl_phone |
#
  #@blacklist
  #Scenario Outline: A Fraud analyst with an in-valid auth, is denied to add blacklisted info to the Company blacklist
    #Given I am a fraud analyst with auth-id '<token>'
    #When I add '<userinfotype>', '<info>' in the company blacklist
    #Then Permission is denied
#
    #Examples: 
      #| token        | userinfotype | info                |
      #| opqG841evbcC | email        | frauduser@gmail.com |
      #| opqG841evbcC | name         | Bin Laden           |
      #| opqG841evbcC | phone        |          3104200000 |

	@blacklist @bl_check
  Scenario Outline: If a new customer tries to sign up and he is in the company blacklist then deny sign-up with appropriate blacklist codes.
    Given I am a new user and my '<userinfotype>', '<info>' exists in your company blacklist
    When I sign up using my '<userinfotype>'
    Then you check it against your company blacklist
    And Status code '<code>' is returned

    Examples: 
      | userinfotype | info                | code     |
      | email        | frauduser@gmail.com | bl_email |
      | name         | Bin Laden           | bl_name  |
      | phone        |          3104200000 | bl_phone |

  @blacklist
  Scenario Outline: If Customer tries to sign up and he is NOT the company blacklist then approve.
    Given I am a new user and my '<userinfotype>', '<info>' does not exist in your company blacklist
    When I sign up using my '<userinfotype>'
    Then you check it against your company blacklist
    And Status code '<code>' is returned

    Examples: 
      | userinfotype | info            | code       |
      | email        | obama@gmail.com | bl_approve |
      | name         | Barak Obama     | bl_approve |
      | phone        |      3101111111 | bl_approve |
