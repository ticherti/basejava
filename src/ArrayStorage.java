/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int firstEmptyCell = 0;

    void clear() {
        for (int i = 0; i < firstEmptyCell; i++) {
            storage[i] = null;
        }
        firstEmptyCell = 0;
    }

    void save(Resume r) {
        if (r == null) return;
        if (firstEmptyCell > storage.length - 1) return;
        storage[firstEmptyCell] = r;
        firstEmptyCell++;
    }

    Resume get(String uuid) {
        if (uuid == null) return null;
        for (int i = 0; i < firstEmptyCell; i++) {
            if (storage[i].uuid.equals(uuid))
                return storage[i];
        }
        return null;
    }

    void delete(String uuid) {
        if (uuid == null) return;
        for (int i = 0; i < firstEmptyCell; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                fillEmptyCell(i);
                break;
            }
        }
        firstEmptyCell--;
    }

    private void fillEmptyCell(int empty) {
        int lastEntry = firstEmptyCell - 1;
        for (int i = empty; i < lastEntry; i++) {
            storage[i] = storage[i + 1];
        }
        storage[lastEntry] = null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] allResumes = new Resume[firstEmptyCell];
        System.arraycopy(storage, 0, allResumes, 0, firstEmptyCell);
        return allResumes;
    }

    int size() {
        return firstEmptyCell;
    }
}
