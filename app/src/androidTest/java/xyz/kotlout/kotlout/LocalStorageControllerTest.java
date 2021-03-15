package xyz.kotlout.kotlout;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.LocalStorageController;
import xyz.kotlout.kotlout.model.user.User;

/**
 * Local storage tests Requires app context to get app path
 */
@RunWith(AndroidJUnit4.class)
public class LocalStorageControllerTest {

  // Idk if there is a better way to keep track of the file name
  private static final String UUID_FILE_NAME = "uuid.dat";
  private static Context ctx;
  private static String previousUuid;

  @BeforeClass
  public static void initLocalStorageTest() {
    ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
    previousUuid = LocalStorageController.readUUID();
  }

  // Restore the state before tests were run
  @AfterClass
  public static void tearDownLocalStorageTest() {
    if (previousUuid != null) {
      LocalStorageController.storeUUID(previousUuid);
    } else {
      // If no file existed, it should be deleted
      File uuidFile = new File(getFilePath());
      uuidFile.delete();
    }
  }

  public static String getFilePath() {
    return ctx.getFilesDir().toString() + '/' + UUID_FILE_NAME;
  }

  public String readStringFromFile() throws IOException {
    byte[] res = new byte[0];
    try (
        FileInputStream fileInputStream = new FileInputStream(getFilePath())
    ) {
      res = new byte[fileInputStream.available()];
      while (fileInputStream.read(res) != -1) {
      }
    }
    String fileString = new String(res);
    if (fileString == null | fileString.isEmpty()) {
      return null;
    }
    return fileString;
  }

  public void writeBytesToFile(byte[] data, boolean append) {
    try (
        FileOutputStream uuidFileStream = new FileOutputStream(getFilePath(), append)
    ) {
      uuidFileStream.write(data);
    } catch (IOException e) {
      assertWithMessage("Error writing bytes to file: " + e.getMessage()).fail();
    }
  }

  public void writeStringToFile(String data, boolean append) {
    try (
        FileWriter uuidFileStream = new FileWriter(getFilePath(), append)
    ) {
      uuidFileStream.write(data);
    } catch (IOException e) {
      assertWithMessage("Error writing string to file: " + e.getMessage()).fail();
    }
  }

  @Before
  public void deleteStoredId() {
    File uuidFile = new File(getFilePath());
    if (uuidFile.exists()) {
      uuidFile.delete();
    }
  }

  @Test
  public void testReadValidId() {
    String TAG = "testReadValidId";
    List<String> Ids = Stream.generate(() -> UUID.randomUUID().toString()).limit(10)
        .collect(Collectors.toList());
    boolean success = true;
    for (String uuid : Ids) {
      writeStringToFile(uuid, false);
      String readId = LocalStorageController.readUUID();
      System.out.println("Testing id: " + uuid);
      if (!readId.equals(uuid)) {
        success = false;
        break;
      }
    }
    assertThat(success).isTrue();

  }

  @Test
  public void testReadNoId() {
    String id = LocalStorageController.readUUID();
    assertThat(id).isNull();
  }

  @Test
  public void testReadEmptyId() {
    File uuidFile = new File(getFilePath());
    try {
      uuidFile.createNewFile();
      String id = LocalStorageController.readUUID();
      if (id != null) {
        assertWithMessage("Non-empty id on read id file: " + uuidFile).fail();
      }
    } catch (IOException e) {
      assertWithMessage("Encountered error reading file: " + e.getMessage()).fail();
    }
  }


  @Test
  @Ignore("Gibberish uuid's actually work I guess")
  public void testReadCorruptId() {
    Random rand = new Random();
    byte[] garbo = new byte[255];
    rand.nextBytes(garbo);
    writeBytesToFile(garbo, false);
    String id = LocalStorageController.readUUID();
    User test = new User("Name", "Last", "test", "phone", id);
    FirebaseController.getFirestore().collection("users").document(id).set(test);
    assertWithMessage("Garbage UUID read as valid: " + id, id).that(id).isNull();
  }

  @Test
  public void testReadMultiLineId() {
    String multiLineId = String
        .format("%s\n%s\n%s", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    writeStringToFile(multiLineId, false);
    String id = LocalStorageController.readUUID();
    assertWithMessage("Multiline UUID read as valid: " + id).that(id).isNull();
  }

  @Test
  public void testStoreValidId() {
    List<String> Ids = Stream.generate(() -> UUID.randomUUID().toString()).limit(10)
        .collect(Collectors.toList());
    boolean success = true;
    for (String id : Ids) {
      LocalStorageController.storeUUID(id);
      try {
        String inputId = readStringFromFile();
        if (!id.equals(inputId)) {
          assertWithMessage("Stored and read Id's differ:\n\tStored: " + id + "\n\t" + inputId)
              .fail();
        }
      } catch (IOException e) {
        assertWithMessage("Failed to read file: " + e.getMessage()).fail();
      }
    }

  }

  @Test(expected = FileNotFoundException.class)
  public void testStoreNullId() throws IOException {
    LocalStorageController.storeUUID(null);
    String id = readStringFromFile();
  }

  @Test(expected = FileNotFoundException.class)
  public void testStoreEmptyId() throws IOException {
    LocalStorageController.storeUUID("");
    String id = readStringFromFile();
  }

}
