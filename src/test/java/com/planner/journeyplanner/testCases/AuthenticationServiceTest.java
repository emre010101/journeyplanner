package com.planner.journeyplanner.testCases;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register_newUser_success() {
        // Prepare input request
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password");

        // Prepare a user object
        User user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Mock dependencies
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // Call the method
        AuthenticationResponse response = authenticationService.register(request);

        // Verify the results
        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        // Optionally, verify the interactions with the dependencies
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(request.getPassword());
        verify(jwtService).generateToken(user);
        verify(jwtService).generateRefreshToken(user);
    }
}
