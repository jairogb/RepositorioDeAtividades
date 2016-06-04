package br.com.repositoriodeatividades.usecases.activities.create;

import br.com.repositoriodeatividades.entities.Exercise;
import br.com.repositoriodeatividades.entities.ExerciseOption;
import br.com.repositoriodeatividades.entities.User;
import br.com.repositoriodeatividades.repositories.interfaces.ExerciseRepositoryInterface;
import br.com.repositoriodeatividades.repositories.interfaces.UserRepositoryInterface;
import br.com.repositoriodeatividades.usecases.activities.create.models.ExerciseClassifier;
import br.com.repositoriodeatividades.usecases.activities.create.models.vo.CreateActivityParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreateActivity {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ExerciseRepositoryInterface exerciseRepository;

    @Autowired
    UserRepositoryInterface userRepository;

    @Autowired
    ExerciseClassifier exerciseClassifier;

    public List<Exercise> execute(List<CreateActivityParameters> createActivityParametersList) throws Exception {

        if(createActivityParametersList.size() == 0) {
            throw new IllegalArgumentException("No data has been passed");
        }

        User user = userRepository.findByUsername(createActivityParametersList.get(0).getUsername());
        if(user == null) {
            throw new IllegalStateException("User not found");
        }

        List<Exercise> finalExerciseList = new ArrayList<>();
        for(CreateActivityParameters createActivityParameters : createActivityParametersList) {

            createActivityParameters.setUser(user);

            try {
                List persistedExercises = exerciseRepository.findAllBy(createActivityParameters);
                List<Map> exercisesClassified = exerciseClassifier.classify(persistedExercises, createActivityParameters.getTags());

                int totalAdded = 0;
                for(int i = 0; totalAdded < createActivityParameters.getAmount() &&  i < exercisesClassified.size(); i++) {
                    Exercise orderedExercise = (Exercise) exercisesClassified.get(i).get("exercise");
                    if(!exerciseAlreadyAdded(orderedExercise, finalExerciseList)) {
                        finalExerciseList.add(orderedExercise);
                        totalAdded++;
                    }
                }

            } catch (Exception e) {
                log.error("Error while extracting exercises");
            }
        }

        long seed = System.nanoTime();
        Collections.shuffle(finalExerciseList, new Random(seed));

        enumerateExercises(finalExerciseList);

        return finalExerciseList;
    }

    private boolean exerciseAlreadyAdded(Exercise orderedExercise, List<Exercise> finalExerciseList) {
        boolean alreadyAdded = false;
        for(Exercise finalExercise : finalExerciseList) {
            if(orderedExercise.getId() == finalExercise.getId())
                alreadyAdded = true;
        }
        return alreadyAdded;
    }

    private void enumerateExercises(List<Exercise> exerciseList) {
        for(int i = 0; i < exerciseList.size(); i++) {
            Exercise exercise = exerciseList.get(i);
            String exerciseLabel = i + 1 + ") " + exercise.getLabel();
            exercise.setLabel(exerciseLabel);
            enumerateExerciseOptions(exercise.getExerciseOptions());
        }
    }

    private void enumerateExerciseOptions(List<ExerciseOption> exerciseOptionList) {
        char enumeration = 'a';
        for(ExerciseOption option : exerciseOptionList) {
            String optionLabel = enumeration + ") " + option.getLabel();
            option.setLabel(optionLabel);
            enumeration++;
        }
    }

}
