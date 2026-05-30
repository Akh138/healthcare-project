package com.healthcare.userservice.service;

import com.healthcare.userservice.model.User;
import com.healthcare.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ICI JE TESTE LA LOGIQUE DE MON SERVICE UTILISATEUR.
 * Le but est de vérifier que mon code Java fonctionne bien sans avoir besoin de Docker ou MySQL.
 */
@ExtendWith(MockitoExtension.class) // Je dis à JUnit d'utiliser Mockito pour les simulateurs
public class UserServiceTest {

    // Je crée un "faux" Repository. C'est mon simulateur de base de données.
    @Mock
    private UserRepository userRepository;

    // Je crée mon "vrai" Service et je demande à Mockito d'y injecter le simulateur ci-dessus.
    @InjectMocks
    private UserService userService;

    // --- TEST 1 : VÉRIFIER QU'ON RÉCUPÈRE BIEN TOUTE LA LISTE ---
    @Test
    public void testGetAllUsers_ShouldReturnList() {
        // 1. PRÉPARER : Je crée une fausse liste avec 2 utilisateurs à l'intérieur
        List<User> fausseListe = Arrays.asList(new User(), new User());

        // Je dis au simulateur : "Quand le service te demande tout, donne-lui ma fausse liste"
        when(userRepository.findAll()).thenReturn(fausseListe);

        // 2. AGIR : J'appelle ma méthode de service
        List<User> resultat = userService.getAllUsers();

        // 3. VÉRIFIER : Je vérifie qu'il y a bien 2 éléments dans la liste reçue
        assertEquals(2, resultat.size());
        // Je vérifie que le service a bien appelé la base de données 1 seule fois
        verify(userRepository, times(1)).findAll();
    }

    // --- TEST 2 : VÉRIFIER QU'ON TROUVE UN UTILISATEUR PAR SON ID ---
    @Test
    public void testGetUserById_ShouldReturnUser() {
        // 1. PRÉPARER : Je crée un utilisateur avec l'ID 10
        User userDeTest = new User(10L, "test_admin", "mdp", "ADMIN");

        // Je simule le comportement : "Si on cherche l'ID 10, renvoie mon utilisateur"
        when(userRepository.findById(10L)).thenReturn(Optional.of(userDeTest));

        // 2. AGIR
        Optional<User> resultat = userService.getUserById(10L);

        // 3. VÉRIFIER : Je vérifie qu'on a bien trouvé quelque chose et que c'est le bon pseudo
        assertTrue(resultat.isPresent());
        assertEquals("test_admin", resultat.get().getUsername());
    }

    // --- TEST 3 : VÉRIFIER LA RECHERCHE PAR PSEUDO (IMPORTANT POUR LE LOGIN) ---
    @Test
    public void testGetUserByUsername_ShouldReturnUser() {
        // 1. PRÉPARER
        User userHabib = new User(1L, "habib", "123", "DOCTOR");
        when(userRepository.findByUsername("habib")).thenReturn(Optional.of(userHabib));

        // 2. AGIR
        Optional<User> resultat = userService.getUserByUsername("habib");

        // 3. VÉRIFIER : On vérifie que c'est bien un médecin qui est trouvé
        assertTrue(resultat.isPresent());
        assertEquals("DOCTOR", resultat.get().getRole());
    }

    // --- TEST 4 : VÉRIFIER LA CRÉATION D'UN COMPTE ---
    @Test
    public void testCreateUser_ShouldReturnSavedUser() {
        // 1. PRÉPARER : Un utilisateur sans ID (ce qui arrive du formulaire)
        User nouveauUser = new User(null, "nouveau", "123", "PATIENT");
        // L'utilisateur tel qu'il ressort de la base avec un ID
        User userEnregistre = new User(1L, "nouveau", "123", "PATIENT");

        when(userRepository.save(any(User.class))).thenReturn(userEnregistre);

        // 2. AGIR
        User resultat = userService.createUser(nouveauUser);

        // 3. VÉRIFIER : Est-ce qu'on a bien un ID maintenant ?
        assertNotNull(resultat.getId());
        assertEquals("nouveau", resultat.getUsername());
    }

    // --- TEST 5 : VÉRIFIER LA MISE À JOUR (UPDATE) ---
    @Test
    public void testUpdateUser_ShouldReturnUpdatedUser() {
        // 1. PRÉPARER : Un utilisateur qui existe déjà
        User userExistant = new User(1L, "vieux_nom", "123", "DOCTOR");
        // Les nouvelles infos à enregistrer
        User nouvellesInfos = new User(1L, "nom_a_jour", "456", "ADMIN");

        // On simule : d'abord on le trouve, ensuite on le sauvegarde
        when(userRepository.findById(1L)).thenReturn(Optional.of(userExistant));
        when(userRepository.save(any(User.class))).thenReturn(nouvellesInfos);

        // 2. AGIR
        User resultat = userService.updateUser(1L, nouvellesInfos);

        // 3. VÉRIFIER : Est-ce que les modifs sont bien là ?
        assertEquals("nom_a_jour", resultat.getUsername());
        assertEquals("ADMIN", resultat.getRole());
    }

    // --- TEST 6 : VÉRIFIER LA SUPPRESSION ---
    @Test
    public void testDeleteUser_ShouldCallDelete() {
        // 1. AGIR : Je demande de supprimer l'ID 1
        userService.deleteUser(1L);

        // 2. VÉRIFIER : Je vérifie juste que le service a bien passé l'ordre au Repository
        verify(userRepository, times(1)).deleteById(1L);
    }
}